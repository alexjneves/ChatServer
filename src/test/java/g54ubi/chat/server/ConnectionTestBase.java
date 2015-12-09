package g54ubi.chat.server;

import g54ubi.chat.common.IChatClient;
import g54ubi.chat.common.IResourceListener;
import g54ubi.chat.common.IResourceReceivedListener;
import org.junit.Before;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.*;

public class ConnectionTestBase {
    protected static final String LIST_COMMAND = "LIST";
    protected static final String STAT_COMMAND = "STAT";
    protected static final String IDENTITY_COMMAND = "IDEN";
    protected static final String HAIL_COMMAND = "HAIL";
    protected static final String MESSAGE_COMMAND = "MESG";
    protected static final String QUIT_COMMAND = "QUIT";

    protected static final String VALID_USER_NAME = "User";
    protected static final String VALID_MESSAGE = "Message";

    protected static final String UNICODE_STRING = "Öæÿ»»¿";

    @Mock
    protected IResourceListener<String> mockMessageListener;
    @Mock
    protected IChatClient mockChatClient;
    @Mock
    protected IChatServer mockChatServer;

    protected Connection connection;
    protected IResourceReceivedListener<String> messageReceivedListener;
    protected String receivedMessage;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        // Capture any messages sent to the client from the Connection in "receivedMessage"
        doAnswer(invocation -> receivedMessage = invocation.getArgumentAt(0, String.class))
                .when(mockChatClient)
                .sendMessage(anyString());

        // Capture the messageReceivedListener so we can send messages from the client
        doAnswer(invocation -> messageReceivedListener = invocation.getArgumentAt(0, IResourceReceivedListener.class))
                .when(mockMessageListener)
                .listen(any());

        initialiseRegisteredConnection();
    }

    protected void initialiseRegisteredConnection() {
        initialiseUnregisteredConnection();
        sendCommand(IDENTITY_COMMAND, VALID_USER_NAME);
    }

    protected void initialiseUnregisteredConnection() {
        connection = new Connection(mockChatClient, mockChatServer, mockMessageListener);
        connection.run();
    }

    protected void sendCommand(final String command, final String args) {
        messageReceivedListener.onResourceReceived(command + " " + args);
    }

    protected void assertChatClientReceivedExpectedMessage(final String expectedMessage) {
        assertThat(receivedMessage, is(equalTo(expectedMessage)));
    }

    protected void assertChatClientReceivedErrorMessage() {
        assertThat(isErrorMessage(receivedMessage), is(true));
    }

    protected void assertChatClientReceivedSuccessMessage() {
        assertThat(isSuccessMessage(receivedMessage), is(true));
    }

    protected boolean isErrorMessage(final String message) {
        return message.contains("BAD");
    }

    protected boolean isSuccessMessage(final String message) {
        return !isErrorMessage(message);
    }

    protected void sendCommand(final String command) {
        messageReceivedListener.onResourceReceived(command);
    }

    protected void sendMessage(final String message, final int numberOfTimes) {
        for (int i = 0; i < numberOfTimes; ++i) {
            sendCommand(HAIL_COMMAND, message);
        }
    }
}
