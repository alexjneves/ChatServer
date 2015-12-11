package g54ubi.chat.integration;

import g54ubi.chat.client.ChatSessionFactory;
import g54ubi.chat.server.ChatServerFactory;
import g54ubi.chat.server.IChatServer;
import org.junit.*;

import java.util.ArrayList;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public final class ChatServerIntegrationTests {
    private static final String SERVER_ADDRESS = "localhost";
    private static final int SERVER_PORT = 8999;
    private static final long TIMEOUT_SECONDS = 3;

    private final static String USER_1_NAME = "User1";
    private final static String USER_2_NAME = "User2";

    private ArrayList<IntegrationTestChatSession> allClients;
    private ChatSessionFactory chatSessionFactory;

    private IntegrationTestChatSession client1;
    private IntegrationTestChatSession client2;

    @BeforeClass
    public static void serverSetUp() {
        final ChatServerFactory chatServerFactory = new ChatServerFactory(SERVER_PORT);
        final IChatServer chatServer = chatServerFactory.create();

        new Thread(chatServer::start).start();
    }

    @Before
    public void setUp() {
        allClients = new ArrayList<>();
        chatSessionFactory = new ChatSessionFactory(SERVER_ADDRESS, SERVER_PORT);

        client1 = createClient();
        client2 = createClient();

        client1.start();
        client2.start();

        client1.setUserName(USER_1_NAME);
        client2.setUserName(USER_2_NAME);
    }

    @After
    public void tearDown() {
        for (final IntegrationTestChatSession client : allClients) {
            client.quit();
            client.stop();
        }
    }

    @Test
    public void listAllUsers_ReturnsExpectedUserList() {
        final String expectedUserList ="OK " + USER_1_NAME + ", " + USER_2_NAME + ", ";

        client1.listCurrentUsers();

        assertClientReceivedResponse(client1, expectedUserList);
    }

    @Test
    public void getSessionStatistics_ReturnsExpectedStatistics() {
        final int numberOfMessages = 3;
        final String expectedStatistics = "OK There are currently 2 user(s) on the server You are logged in and have sent " + numberOfMessages + " message(s)";

        for (int i = 0; i < numberOfMessages; ++i) {
            client2.broadcastMessage("", client1);
        }

        client2.getSessionStatistics();

        assertClientReceivedResponse(client2, expectedStatistics);
    }

    @Test
    public void quit_ReturnsExpectedMessage() {
        final IntegrationTestChatSession chatSession = new IntegrationTestChatSession(chatSessionFactory, TIMEOUT_SECONDS);
        chatSession.start();

        chatSession.quit();

        final String expectedQuitMessage = "OK goodbye";

        assertClientReceivedResponse(chatSession, expectedQuitMessage);
    }

    @Test
    public void setUserName_ReturnsExpectedMessageContainingTheUserName() {
        final IntegrationTestChatSession chatSession = createClient();
        chatSession.start();

        final String expectedUserName = "setUserName_ReturnsExpectedMessageContainingTheUserName";

        chatSession.setUserName(expectedUserName);

        assertClientReceivedResponse(chatSession, "OK Welcome to the chat server " + expectedUserName);
    }

    @Test
    public void broadcastMessage_OtherClientsReceiveBroadcast() {
        final String expectedMessage = "Expected Message";

        client1.broadcastMessage(expectedMessage, client2);

        assertClientReceivedResponse(client2, "Broadcast from " + USER_1_NAME + ": " + expectedMessage);
    }

    @Test
    public void sendPrivateMessage_RecipientReceivesMessage() {
        final String expectedPrivateMessage = "Message";

        client1.sendPrivateMessage(client2, expectedPrivateMessage);

        assertClientReceivedResponse(client2, "PM from " + USER_1_NAME + ":" + expectedPrivateMessage);
    }

    private void assertClientReceivedResponse(final IntegrationTestChatSession chatSession, final String expectedResponse) {
        boolean contains = false;

        for (final String response : chatSession.getReceivedServerResponses()) {
            if (response.equals(expectedResponse)) {
                contains = true;
                break;
            }
        }

        assertThat(contains, is(true));
    }

    private IntegrationTestChatSession createClient() {
        final IntegrationTestChatSession client = new IntegrationTestChatSession(chatSessionFactory, TIMEOUT_SECONDS);
        allClients.add(client);

        return client;
    }
}
