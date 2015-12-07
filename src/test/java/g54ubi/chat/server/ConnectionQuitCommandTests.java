package g54ubi.chat.server;

import org.junit.Test;

import java.io.IOException;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public final class ConnectionQuitCommandTests extends ConnectionTestBase {
    @Test
    public void quitCommand_WhenUnregistered_ClientReceivesSuccessMessage() {
        initialiseUnregisteredConnection();

        sendCommand(QUIT_COMMAND);

        assertChatClientReceivedSuccessMessage();
    }

    @Test
    public void quitCommand_WhenRegistered_ClientReceivesSuccessMessage() {
        sendCommand(QUIT_COMMAND);

        assertChatClientReceivedSuccessMessage();
    }

    @Test
    public void quitCommand_WhenRegistered_AndClientHasNotSentAnyMessages_ClientReceivesMessageWithExpectedMessageCount() {
        sendCommand(QUIT_COMMAND);

        final String expectedMessageCount = "0";

        assertThat(receivedMessage.contains((expectedMessageCount)), is(true));
    }

    @Test
    public void quitCommand_WhenRegistered_AndClientHasSentMessages_ClientReceivesMessageWithExpectedMessageCount() {
        final int expectedMessageCount = 5;
        sendMessage(VALID_MESSAGE, expectedMessageCount);

        sendCommand(QUIT_COMMAND);

        assertThat(receivedMessage.contains(Integer.toString(expectedMessageCount)), is(true));
    }

    @Test
    public void quitCommand_StopsMessageListener() {
        sendCommand(QUIT_COMMAND);

        verify(mockMessageListener, times(1)).stop();
    }

    @Test
    public void quitCommand_StopsChatClient() throws IOException {
        sendCommand(QUIT_COMMAND);

        verify(mockChatClient, times(1)).close();
    }

    @Test
    public void quitCommand_RemovesDeadUsersFromChatServer() {
        sendCommand(QUIT_COMMAND);

        verify(mockChatServer, times(1)).removeDeadUsers();
    }
}
