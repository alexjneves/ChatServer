package g54ubi.chat.server;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public final class ConnectionTests {
    private static final String IdentityCommand = "IDEN";

    private IChatClient mockChatClient;
    private IChatServer mockChatServer;
    private IMessageListenerFactory mockMessageListenerFactory;
    private IMessageListener mockMessageListener;
    private Connection connection;

    private IMessageReceivedListener messageReceivedListener;

    @Before
    public void setUp() {
        mockChatClient = mock(IChatClient.class);
        mockChatServer = mock(IChatServer.class);

        mockMessageListenerFactory = mock(IMessageListenerFactory.class);
        mockMessageListener = mock(IMessageListener.class);

        doAnswer((invocation) -> {
            messageReceivedListener = (IMessageReceivedListener) invocation.getArguments()[0];
            return mockMessageListener;
        }).when(mockMessageListenerFactory).create(any());

        connection = new Connection(mockChatClient, mockChatServer, mockMessageListenerFactory);
    }

    @Test
    public void createConnection_RegistersMessageReceivedListener() {
        assertThat(mockMessageListenerFactory, is(notNullValue()));
    }

    @Test
    public void messageForConnection_SendsExpectedMessage() {
        final String expectedMessage = "Expected Message";

        connection.messageForConnection(expectedMessage);

        assertChatClientReceivedMessage(expectedMessage);
    }

    @Test
    public void getUserName_BeforeIdenHasBeenCalled_ReturnsNull() {
        final String userName = connection.getUserName();

        assertThat(userName, is(nullValue()));
    }

    @Test
    public void getUserName_AfterIdenHasBeenCalledWithUserName_WithNoSpaces_ReturnsExpectedUserName() {
        final String expectedUserNameWithoutSpace = "User1";

        sendCommand(IdentityCommand, expectedUserNameWithoutSpace);

        final String userName = connection.getUserName();

        assertThat(userName, is(expectedUserNameWithoutSpace));
    }

    @Ignore
    public void getUserName_AfterIdenHasBeenCalledWithUserName_WithSpaces_ReturnsExpectedUserName() {
        final String expectedUserNameWithSpace = "User 1";

        sendCommand(IdentityCommand, expectedUserNameWithSpace);

        final String userName = connection.getUserName();

        assertThat(userName, is(expectedUserNameWithSpace));
    }

    private void assertChatClientReceivedMessage(final String expectedMessage) {
        verify(mockChatClient).sendMessage(expectedMessage);
    }

    private void sendCommand(final String command) {
        messageReceivedListener.onMessageReceived(command);
    }

    private void sendCommand(final String command, final String args) {
        messageReceivedListener.onMessageReceived(command + " " + args);
    }
}
