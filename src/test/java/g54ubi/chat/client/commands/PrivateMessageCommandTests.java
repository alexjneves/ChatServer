package g54ubi.chat.client.commands;

import g54ubi.chat.common.CommandConstants;
import org.junit.Test;

public class PrivateMessageCommandTests extends CommandTestBase {
    private String recipient = "Recipient";
    private String messageToSend = "Message";

    private String expectedRecipient = recipient;
    private String expectedMessageToSend = messageToSend;

    @Override
    protected IChatServerCommand createCommand() {
        return new PrivateMessageCommand(recipient, messageToSend);
    }

    @Override
    protected String getExpectedMessage() {
        return CommandConstants.MESG + " " + expectedRecipient + " " + expectedMessageToSend;
    }

    @Test
    public void whenRecipientContainsUnicode_FormsExpectedMessage() {
        recipient = "?VØT³";
        expectedRecipient = recipient;
        assertCommandFormsExpectedMessage();
    }

    @Test
    public void whenRecipientIsEmpty_FormsExpectedMessage() {
        recipient = "";
        expectedRecipient = recipient;
        assertCommandFormsExpectedMessage();
    }

    @Test
    public void whenRecipientContainsNoWhitespace_FormsExpectedMessage() {
        recipient = "User";
        expectedRecipient = recipient;
        assertCommandFormsExpectedMessage();
    }

    @Test
    public void whenRecipientContainsLeadingWhitespace_WhitespaceIsRemoved() {
        recipient = " User";
        expectedRecipient = "User";
        assertCommandFormsExpectedMessage();
    }

    @Test
    public void whenRecipientContainsTrailingWhitespace_WhitespaceIsRemoved() {
        recipient = "User ";
        expectedRecipient = "User";
        assertCommandFormsExpectedMessage();
    }

    @Test
    public void whenRecipientContainsWhitespaceInTheMiddle_WhitespaceIsRemoved() {
        recipient = "User Name";
        expectedRecipient = "UserName";
        assertCommandFormsExpectedMessage();
    }

    @Test
    public void whenMessageContainsUnicode_FormsExpectedMessage() {
        messageToSend = "õ_ßõÿ";
        expectedMessageToSend = messageToSend;
        assertCommandFormsExpectedMessage();
    }

    @Test
    public void whenMessageIsEmpty_FormsExpectedMessage() {
        messageToSend = "";
        expectedMessageToSend = messageToSend;
        assertCommandFormsExpectedMessage();
    }

    @Test
    public void whenMessageContainsNoWhitespace_FormsExpectedMessage() {
        messageToSend = "Message";
        expectedMessageToSend = messageToSend;
        assertCommandFormsExpectedMessage();
    }

    @Test
    public void whenMessageContainsLeadingWhitespace_WhitespaceIsRemoved() {
        messageToSend = " Message";
        expectedMessageToSend = "Message";
        assertCommandFormsExpectedMessage();
    }

    @Test
    public void whenMessageContainsTrailingWhitespace_WhitespaceIsRemoved() {
        messageToSend = "Message ";
        expectedMessageToSend = "Message";
        assertCommandFormsExpectedMessage();
    }

    @Test
    public void whenMessageContainsWhitespaceInTheMiddle_WhitespaceIsNotRemoved() {
        messageToSend = "This is my message";
        expectedMessageToSend = messageToSend;
        assertCommandFormsExpectedMessage();
    }
}
