package g54ubi.chat.server;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.*;

public final class ConnectionTests {
    private static final String IDENTITY_COMMAND = "IDEN";
    private static final String validUserName = "User";

    private IChatClient mockChatClient;
    private IChatServer mockChatServer;
    private IMessageListenerFactory mockMessageListenerFactory;
    private IMessageListener mockMessageListener;
    private Connection connection;

    private IMessageReceivedListener messageReceivedListener;
    private String receivedMessage;

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

        connection = new Connection(mockChatClient, mockChatServer, mockMessageListenerFactory);
    }

    @Test
    public void createConnection_RegistersMessageReceivedListener() {
        assertThat(mockMessageListenerFactory, is(notNullValue()));
    }

    @Test
    public void messageForConnection_SendsExpectedMessageToClient() {
        final String expectedMessage = "Expected Message";

        connection.messageForConnection(expectedMessage);

        assertChatClientReceivedExpectedMessage(expectedMessage);
    }

    @Test
    public void getUserName_BeforeIdenHasBeenCalled_ReturnsNull() {
        final String actualUserName = connection.getUserName();

        assertThat(actualUserName, is(nullValue()));
    }

    @Test
    public void identityCommand_WhenUnregistered_WithUserNameWithNoSpaces_SetsExpectedUserName() {
        final String expectedUserNameWithoutSpace = "User1";

        sendCommand(IDENTITY_COMMAND, expectedUserNameWithoutSpace);

        final String actualUserName = connection.getUserName();

        assertThat(actualUserName, is(expectedUserNameWithoutSpace));
    }

    @Ignore
    public void identityCommand_WhenUnregistered_WithUserNameWithSpaceSeparator_SetsExpectedUserName() {
        final String expectedUserNameWithSpace = "User 1";

        sendCommand(IDENTITY_COMMAND, expectedUserNameWithSpace);

        final String actualUserName = connection.getUserName();

        assertThat(actualUserName, is(expectedUserNameWithSpace));
    }

    @Ignore
    public void identityCommand_WhenUnregistered_WithUserNameWithPrecedingSpace_SetsExpectedUserName() {
        final String expectedUserNameWithPrecedingSpace = " User";

        sendCommand(IDENTITY_COMMAND, expectedUserNameWithPrecedingSpace);

        final String actualUserName = connection.getUserName();

        assertThat(actualUserName, is(expectedUserNameWithPrecedingSpace));
    }

    @Test
    public void identityCommand_WhenUnregistered_AndUserDoesNotExist_ClientReceivesSuccessMessage() {
        sendCommand(IDENTITY_COMMAND, validUserName);

        assertChatClientReceivedSuccessMessage();
    }

    @Test
    public void identityCommand_WhenUnregistered_AndUserDoesNotExist_StateChangesToRegistered() {
        sendCommand(IDENTITY_COMMAND, validUserName);

        final int expectedState = Connection.STATE_REGISTERED;
        final int actualState = connection.getState();

        assertThat(actualState, is(equalTo(expectedState)));
    }

    @Test
    public void identityCommand_WhenUnregistered_AndUserDoesExist_ClientReceivesErrorMessage() {
        when(mockChatServer.doesUserExist(validUserName)).thenReturn(true);

        sendCommand(IDENTITY_COMMAND, validUserName);

        assertChatClientReceivedErrorMessage();
    }

    @Test
    public void identityCommand_WhenAlreadyRegistered_ClientReceivesErrorMessage() {
        sendCommand(IDENTITY_COMMAND, validUserName);

        assertThat(connection.getState(), is(equalTo(Connection.STATE_REGISTERED)));

        sendCommand(IDENTITY_COMMAND, validUserName);

        assertChatClientReceivedErrorMessage();
    }

    private void assertChatClientReceivedExpectedMessage(final String expectedMessage) {
        verify(mockChatClient).sendMessage(expectedMessage);
    }

    private void sendCommand(final String command) {
        messageReceivedListener.onMessageReceived(command);
    }

    private void sendCommand(final String command, final String args) {
        messageReceivedListener.onMessageReceived(command + " " + args);
    }

    private void assertChatClientReceivedErrorMessage() {
        assertThat(isErrorMessage(receivedMessage), is(equalTo(true)));
    }

    private void assertChatClientReceivedSuccessMessage() {
        assertThat(isSuccessMessage(receivedMessage), is(equalTo(true)));
    }

    private boolean isErrorMessage(final String message) {
        return message.contains("BAD");
    }

    private boolean isSuccessMessage(final String message) {
        return !isErrorMessage(message);
    }
}
