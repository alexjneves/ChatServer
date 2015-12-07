package g54ubi.chat.server;

import org.junit.Ignore;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public final class ConnectionTests extends ConnectionTestBase {
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
        initialiseUnregisteredConnection();

        final String actualUserName = connection.getUserName();

        assertThat(actualUserName, is(nullValue()));
    }

    @Test
    public void validateMessage_WithInvalidCommand_ClientReceivesErrorMessage() {
        final String invalidCommand = "Invalid";

        sendCommand(invalidCommand);

        assertChatClientReceivedErrorMessage();
    }

    @Ignore
    public void validateMessage_WithNullCommand_ClientReceivesErrorMessage() {
        final String nullCommand = null;

        sendCommand(nullCommand);

        assertChatClientReceivedErrorMessage();
    }

    @Test
    public void run_SetsRunningToTrue() {
        connection.run();

        assertThat(connection.isRunning(), is(true));
    }

    @Test
    public void run_StartsMessageListener() {
        final IMessageListener messageListener = mock(IMessageListener.class);

        connection = new Connection(mockChatClient, mockChatServer, messageListener);
        connection.run();

        verify(mockMessageListener, times(1)).listen(any());
    }
}
