package g54ubi.chat.client.command;

import g54ubi.chat.client.commands.QuitCommand;
import g54ubi.chat.common.CommandConstants;
import org.junit.Before;
import org.junit.Test;

public final class QuitCommandTests extends CommandTestBase {
    private QuitCommand quitCommand;

    @Before
    public void setUp() {
        quitCommand = new QuitCommand();
    }

    @Test
    public void asServerMessage_ReturnsExpectedMessage() {
        assertCommandFormsExpectedMessage(quitCommand, CommandConstants.QUIT);
    }
}
