package g54ubi.chat.integration;

import g54ubi.chat.client.ChatSessionFactory;
import g54ubi.chat.client.IChatSession;
import g54ubi.chat.client.IChatSessionFactory;
import g54ubi.chat.server.ChatServerFactory;
import g54ubi.chat.server.IChatServer;
import org.junit.*;

import java.util.ArrayList;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public final class ChatServerIntegrationTests {
    private final static String SERVER_ADDRESS = "localhost";
    private final static int SERVER_PORT = 8999;
    private final static long TIMEOUT_SECONDS = 3;

    private static final String USER_1_NAME = "User1";
    private final static String USER_2_NAME = "User2";

    private IChatSession client1;
    private IChatSession client2;

    private CopyOnWriteArrayList<String> actualClient1ServerResponses;
    private CopyOnWriteArrayList<String> actualClient2ServerResponses;

    private CountDownLatch serverResponseReceived;

    @BeforeClass
    public static void serverSetUp() {
        final ChatServerFactory chatServerFactory = new ChatServerFactory(SERVER_PORT);
        final IChatServer chatServer = chatServerFactory.create();

        new Thread(chatServer::start).start();
    }

    @Before
    public void setUp() {
        actualClient1ServerResponses = new CopyOnWriteArrayList<>();
        actualClient2ServerResponses = new CopyOnWriteArrayList<>();

        final IChatSessionFactory chatSessionFactory = new ChatSessionFactory(SERVER_ADDRESS, SERVER_PORT);

        client1 = chatSessionFactory.create((response) -> {
                actualClient1ServerResponses.add(response);
                serverResponseReceived.countDown();

        });

        client2 = chatSessionFactory.create((response) -> {
            actualClient2ServerResponses.add(response);
            serverResponseReceived.countDown();
        });

        client1.start();
        client2.start();

        executeSessionCommand(() -> client1.setUserName(USER_1_NAME));
        executeSessionCommand(() -> client2.setUserName(USER_2_NAME));
    }

    @After
    public void tearDown() {
        executeSessionCommand(client1::quit);
        client1.stop();

        executeSessionCommand(client2::quit);
        client2.stop();
    }

    @Test
    public void listAllUsers_ReturnsExpectedResponse() {
        executeSessionCommand(client1::listCurrentUsers);

        assertClientReceivedResponse(actualClient1ServerResponses, "OK " + USER_1_NAME + ", " + USER_2_NAME + ", ");
    }

    @Test
    public void broadcastMessage_OtherClientsReceiveBroadcast() {
        final String expectedMessage = "Expected Message";

        executeSessionCommand(() -> client1.broadcastMessage(expectedMessage), 2);

        assertClientReceivedResponse(actualClient2ServerResponses, "Broadcast from " + USER_1_NAME + ": " + expectedMessage);
    }

    @Test
    public void sendPrivateMessage_RecipientReceivesMessage() {
        final String expectedPrivateMessage = "Message";

        executeSessionCommand(() -> client1.sendPrivateMessage(USER_2_NAME, expectedPrivateMessage), 2);

        assertClientReceivedResponse(actualClient2ServerResponses, "PM from " + USER_1_NAME + ":" + expectedPrivateMessage);
    }

    private void executeSessionCommand(final IChatSessionCommand command) {
        executeSessionCommand(command, 1);
    }

    private void executeSessionCommand(final IChatSessionCommand command, final int numberOfExpectedResponses) {
        serverResponseReceived = new CountDownLatch(numberOfExpectedResponses);
        command.execute();
        awaitServerResponse();
    }

    private void awaitServerResponse() {
        try {
            serverResponseReceived.await(TIMEOUT_SECONDS, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
            assertThat("Timed out waiting for server response", true, is(false));
        }
    }

//    private void assertResponsesMatchExpected() {
//        assertThat(expectedClient2ServerResponses, is(equalTo(expectedClient2ServerResponses)));
//    }

    private void assertClientReceivedResponse(final CopyOnWriteArrayList<String> clientResponses, final String expectedResponse) {
        boolean contains = false;

        for (final String response : clientResponses) {
            if (response.equals(expectedResponse)) {
                contains = true;
                break;
            }
        }

        assertThat(contains, is(true));
    }

    interface IChatSessionCommand {
        void execute();
    }
}
