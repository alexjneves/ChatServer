package g54ubi.chat.integration;

import g54ubi.chat.client.ChatSessionFactory;
import g54ubi.chat.client.IChatSession;
import g54ubi.chat.client.IChatSessionFactory;
import g54ubi.chat.server.ChatServerFactory;
import g54ubi.chat.server.IChatServer;
import org.junit.*;

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

    private CopyOnWriteArrayList<String> actualClient1ServerResponses;
    private CopyOnWriteArrayList<String> actualClient2ServerResponses;

    private CountDownLatch serverResponseReceived;

    private IChatSession client1;
    private IChatSession client2;

    private int user1MessageCount = 0;
    private int user2MessageCount = 0;

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
        serverResponseReceived = new CountDownLatch(0);

        final IChatSessionFactory chatSessionFactory = new ChatSessionFactory(SERVER_ADDRESS, SERVER_PORT);

        client1 = chatSessionFactory.create(this::client1ResponseReceived);
        client2 = chatSessionFactory.create(this::client2ResponseReceived);

        executeSessionCommand(client1::start);
        executeSessionCommand(client2::start);

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

    private void client1ResponseReceived(final String response) {
        actualClient1ServerResponses.add(response);
        System.out.println("USER1(" + ++user1MessageCount + "):" + response);
        serverResponseReceived();
    }

    private void client2ResponseReceived(final String response) {
        actualClient2ServerResponses.add(response);
        System.out.println("USER2(" + ++user2MessageCount + "):" + response);
        serverResponseReceived();
    }

    private synchronized void serverResponseReceived() {
        serverResponseReceived.countDown();
    }

    @Test
    public void listAllUsers_ReturnsExpectedResponse() {
        executeSessionCommand(client1::listCurrentUsers);

        assertClientReceivedResponse(actualClient1ServerResponses, "OK " + USER_1_NAME + ", " + USER_2_NAME + ", ");
    }

    @Test
    public void broadcastMessage_OtherClientsReceiveBroadcast() {
        final String expectedMessage = "Expected Message";

        executeSessionCommand(() -> client1.broadcastMessage(expectedMessage), 4);

        assertClientReceivedResponse(actualClient2ServerResponses, "Broadcast from " + USER_1_NAME + ": " + expectedMessage);
    }

    @Test
    public void sendPrivateMessage_RecipientReceivesMessage() {
        final String expectedPrivateMessage = "Message";

        executeSessionCommand(() -> client1.sendPrivateMessage(USER_2_NAME, expectedPrivateMessage), 3);

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
        boolean timedOut = true;

        try {
            timedOut = !serverResponseReceived.await(TIMEOUT_SECONDS, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();

        }

        assertThat("Timed out waiting for server response", timedOut, is(false));
    }

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
