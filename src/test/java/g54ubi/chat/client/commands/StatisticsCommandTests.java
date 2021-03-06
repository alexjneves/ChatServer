package g54ubi.chat.client.commands;

import g54ubi.chat.common.CommandConstants;
import org.junit.Test;

public final class StatisticsCommandTests extends CommandTestBase {
    @Override
    protected IChatServerCommand createCommand() {
        return new StatisticsCommand();
    }

    @Override
    protected String getExpectedMessage() {
        return CommandConstants.STAT;
    }

    @Test
    public void statisticsCommand_formsExpectedMessage() {
        assertCommandFormsExpectedMessage();
    }
}
