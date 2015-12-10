package g54ubi.chat.client.commands;

import g54ubi.chat.common.CommandConstants;
import org.junit.Test;

public final class QuitCommandTests extends CommandTestBase {
    @Override
    protected IChatServerCommand createCommand() {
        return new QuitCommand();
    }

    @Override
    protected String getExpectedMessage() {
        return CommandConstants.QUIT;
    }

    @Test
    public void quitCommand_formsExpectedMessage() {
        assertCommandFormsExpectedMessage();
    }
}
