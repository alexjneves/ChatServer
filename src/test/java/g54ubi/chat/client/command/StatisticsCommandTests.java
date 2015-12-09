package g54ubi.chat.client.command;

import g54ubi.chat.client.commands.StatisticsCommand;
import g54ubi.chat.common.CommandConstants;
import org.junit.Before;
import org.junit.Test;

public final class StatisticsCommandTests extends CommandTestBase {
    private StatisticsCommand statisticsCommand;

    @Before
    public void setUp() {
        statisticsCommand = new StatisticsCommand();
    }

    @Test
    public void asServerMessage_ReturnsExpectedMessage() {
        assertCommandFormsExpectedMessage(statisticsCommand, CommandConstants.STAT);
    }
}
