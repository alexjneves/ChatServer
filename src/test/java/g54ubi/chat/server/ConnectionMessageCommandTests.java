package g54ubi.chat.server;

import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.*;

public final class ConnectionMessageCommandTests extends ConnectionTestBase {
    private static final String RECIPIENT = "Bob";
    private static final String PERSONAL_MESSAGE = "Hello";
    private static final String VALID_MESSAGE = RECIPIENT + " " + PERSONAL_MESSAGE;

    private String privateMessage;

    @Override
    @Before
    public void setUp() {
        super.setUp();

        when(mockChatServer.sendPrivateMessage(anyString(), eq(RECIPIENT)))
                .then(invocation -> {
                            privateMessage = invocation.getArgumentAt(0, String.class);
                            return true;
                        }
                );
    }

    @Test
    public void messageCommand_WhenUnregistered_ClientReceivesErrorMessage() {
        initialiseUnregisteredConnection();

        sendCommand(MESSAGE_COMMAND, VALID_MESSAGE);

        assertChatClientReceivedErrorMessage();
    }

    @Test
    public void messageCommand_WhenRegistered_WithInvalidMessage_ClientReceivesErrorMessage() {
        final String invalidMessage = "InvalidMessageAsThereIsNoSpace";

        sendCommand(MESSAGE_COMMAND, invalidMessage);

        assertChatClientReceivedErrorMessage();
    }

    @Test
    public void messageCommand_WhenRegistered_WithValidMessage_SendPrivateMessageIsCalled() {
        sendCommand(MESSAGE_COMMAND, VALID_MESSAGE);

        verify(mockChatServer, times(1)).sendPrivateMessage(anyString(), anyString());
    }

    @Test
    public void messageCommand_WhenRegistered_WithValidMessage_SendPrivateMessageIsCalledWithExpectedRecipient() {
        sendCommand(MESSAGE_COMMAND, VALID_MESSAGE);

        final String expectedRecipient = RECIPIENT;

        verify(mockChatServer, times(1)).sendPrivateMessage(anyString(), eq(expectedRecipient));
    }

    @Test
    public void messageCommand_WhenRegistered_WithValidMessage_SendPrivateMessageIsCalledWithExpectedSenderName() {
        sendCommand(MESSAGE_COMMAND, VALID_MESSAGE);

        final String expectedUserName = VALID_USER_NAME;

        assertThat(privateMessage.contains(expectedUserName), is(true));
    }

    @Test
    public void messageCommand_WhenRegistered_WithValidMessage_SendPrivateMessageIsCalledWithExpectedMessageContents() {
        sendCommand(MESSAGE_COMMAND, VALID_MESSAGE);

        final String expectedMessageContents = PERSONAL_MESSAGE;

        assertThat(privateMessage.contains(expectedMessageContents), is(true));
    }

    @Test
    public void messageCommand_WhenRegistered_WithValidMessage_AndSendSucceeds_ClientReceivesSuccessMessage() {
        when(mockChatServer.sendPrivateMessage(anyString(), eq(RECIPIENT))).thenReturn(true);

        sendCommand(MESSAGE_COMMAND, VALID_MESSAGE);

        assertChatClientReceivedSuccessMessage();
    }

    @Test
    public void messageCommand_WhenRegistered_WithValidMessage_AndSendFails_ClientReceivesErrorMessage() {
        when(mockChatServer.sendPrivateMessage(anyString(), eq(RECIPIENT))).thenReturn(false);

        sendCommand(MESSAGE_COMMAND, VALID_MESSAGE);

        assertChatClientReceivedErrorMessage();
    }
}
