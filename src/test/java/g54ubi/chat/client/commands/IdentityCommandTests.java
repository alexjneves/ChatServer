package g54ubi.chat.client.commands;

import g54ubi.chat.common.CommandConstants;
import org.junit.Test;

public final class IdentityCommandTests extends CommandTestBase {
    private String userName;
    private String expectedMessage;

    @Override
    protected IChatServerCommand createCommand() {
        return new IdentityCommand(userName);
    }

    @Override
    protected String getExpectedMessage() {
        return CommandConstants.IDEN + " " + expectedMessage;
    }

    @Test
    public void whenUserNameContainsUnicode_FormsExpectedMessage() {
        userName = "Ü-¶ƒ?";
        expectedMessage = userName;
        assertCommandFormsExpectedMessage();
    }

    @Test
    public void whenUserNameIsEmpty_FormsExpectedMessage() {
        userName = "";
        expectedMessage = userName;
        assertCommandFormsExpectedMessage();
    }

    @Test
    public void whenUserNameContainsNoWhitespace_FormsExpectedMessage() {
        userName = "User";
        expectedMessage = userName;
        assertCommandFormsExpectedMessage();
    }

    @Test
    public void whenUserNameContainsLeadingWhitespace_WhitespaceIsRemoved() {
        userName = " User";
        expectedMessage = "User";
        assertCommandFormsExpectedMessage();
    }

    @Test
    public void whenUserNameContainsTrailingWhitespace_WhitespaceIsRemoved() {
        userName = "User ";
        expectedMessage = "User";
        assertCommandFormsExpectedMessage();
    }

    @Test
    public void whenUserNameContainsWhitespaceInTheMiddle_WhitespaceIsRemoved() {
        userName = "User Name   With  W hite  space";
        expectedMessage = "UserNameWithWhitespace";
        assertCommandFormsExpectedMessage();
    }
}
