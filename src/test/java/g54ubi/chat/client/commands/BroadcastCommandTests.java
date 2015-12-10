package g54ubi.chat.client.commands;

import g54ubi.chat.common.CommandConstants;
import org.junit.Test;

public class BroadcastCommandTests extends CommandTestBase {
    private String messageToBroadcast;
    private String expectedMessage;

    @Override
    protected IChatServerCommand createCommand() {
        return new BroadcastCommand(messageToBroadcast);
    }

    @Override
    protected String getExpectedMessage() {
        return CommandConstants.HAIL + " " + expectedMessage;
    }

    @Test
    public void whenMessageContainsUnicode_FormsExpectedMessage() {
        messageToBroadcast = "³ê+ã±";
        expectedMessage = messageToBroadcast;
        assertCommandFormsExpectedMessage();
    }

    @Test
    public void whenMessageIsEmpty_FormsExpectedMessage() {
        messageToBroadcast = "";
        expectedMessage = messageToBroadcast;
        assertCommandFormsExpectedMessage();
    }

    @Test
    public void whenMessageContainsNoWhitespace_FormsExpectedMessage() {
        messageToBroadcast = "Message";
        expectedMessage = messageToBroadcast;
        assertCommandFormsExpectedMessage();
    }

    @Test
    public void whenMessageContainsLeadingWhitespace_WhitespaceIsRemoved() {
        messageToBroadcast = " Message";
        expectedMessage = "Message";
        assertCommandFormsExpectedMessage();
    }

    @Test
    public void whenMessageContainsTrailingWhitespace_WhitespaceIsRemoved() {
        messageToBroadcast = "Message ";
        expectedMessage = "Message";
        assertCommandFormsExpectedMessage();
    }

    @Test
    public void whenMessageContainsWhitespaceInTheMiddle_WhitespaceIsNotRemoved() {
        messageToBroadcast = "This is my message";
        expectedMessage = messageToBroadcast;
        assertCommandFormsExpectedMessage();
    }
}
