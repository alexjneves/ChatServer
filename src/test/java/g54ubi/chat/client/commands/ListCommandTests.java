package g54ubi.chat.client.commands;

import g54ubi.chat.common.CommandConstants;
import org.junit.Test;

public final class ListCommandTests extends CommandTestBase {
    @Override
    protected IChatServerCommand createCommand() {
        return new ListCommand();
    }

    @Override
    protected String getExpectedMessage() {
        return CommandConstants.LIST;
    }

    @Test
    public void listCommand_FormsExpectedMessage() {
        assertCommandFormsExpectedMessage();
    }
}
