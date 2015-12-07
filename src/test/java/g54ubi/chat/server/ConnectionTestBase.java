package g54ubi.chat.server;

import org.junit.Before;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ConnectionTestBase {
    protected static final String IDENTITY_COMMAND = "IDEN";
    protected static final String LIST_COMMAND = "LIST";
    protected static final String VALID_USER_NAME = "User";

    protected IChatClient mockChatClient;
    protected IChatServer mockChatServer;
    protected IMessageListenerFactory mockMessageListenerFactory;
    protected IMessageListener mockMessageListener;
    protected Connection connection;

    protected IMessageReceivedListener messageReceivedListener;
    protected String receivedMessage;

    @Before
    public void setUp() {
        mockChatClient = mock(IChatClient.class);
        mockChatServer = mock(IChatServer.class);

        // Capture any messages sent to the client from the Connection in "receivedMessage"
        doAnswer(invocation -> {
            receivedMessage = (String) invocation.getArguments()[0];
            return "";
        }).when(mockChatClient).sendMessage(anyString());

        mockMessageListenerFactory = mock(IMessageListenerFactory.class);
        mockMessageListener = mock(IMessageListener.class);

        // Capture the messageReceivedListener so we can send messages from the client
        when(mockMessageListenerFactory.create(any())).thenAnswer(invocation -> {
            messageReceivedListener = (IMessageReceivedListener) invocation.getArguments()[0];
            return mockMessageListener;
        });

        initialiseRegisteredConnection();
    }

    protected void initialiseRegisteredConnection() {
        initialiseUnregisteredConnection();
        sendCommand(IDENTITY_COMMAND, VALID_USER_NAME);
    }

    protected void initialiseUnregisteredConnection() {
        connection = new Connection(mockChatClient, mockChatServer, mockMessageListenerFactory);
    }

    protected void sendCommand(final String command, final String args) {
        messageReceivedListener.onMessageReceived(command + " " + args);
    }

    protected void assertChatClientReceivedExpectedMessage(final String expectedMessage) {
        assertThat(receivedMessage, is(equalTo(expectedMessage)));
    }

    protected void assertChatClientReceivedErrorMessage() {
        assertThat(isErrorMessage(receivedMessage), is(equalTo(true)));
    }

    protected void assertChatClientReceivedSuccessMessage() {
        assertThat(isSuccessMessage(receivedMessage), is(equalTo(true)));
    }

    protected boolean isErrorMessage(final String message) {
        return message.contains("BAD");
    }

    protected boolean isSuccessMessage(final String message) {
        return !isErrorMessage(message);
    }

    protected void sendCommand(final String command) {
        messageReceivedListener.onMessageReceived(command);
    }
}
