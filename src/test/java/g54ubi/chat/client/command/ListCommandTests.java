package g54ubi.chat.client.command;

import g54ubi.chat.client.commands.ListCommand;
import g54ubi.chat.common.CommandConstants;
import org.junit.Before;
import org.junit.Test;

public final class ListCommandTests extends CommandTestBase {
    private ListCommand listCommand;

    @Before
    public void setUp() {
        listCommand = new ListCommand();
    }

    @Test
    public void asServerMessage_ReturnsExpectedMessage() {
        assertCommandFormsExpectedMessage(listCommand, CommandConstants.LIST);
    }
}
