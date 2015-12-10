package g54ubi.chat.client.commands;

import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public final class ChatServerCommandFactoryTests {
    private ChatServerCommandFactory commandFactory;

    @Before
    public void setUp() {
        commandFactory = new ChatServerCommandFactory();
    }

    @Test
    public void createListCommand_ReturnsListCommand() {
        final IChatServerCommand command = commandFactory.createListCommand();

        assertThat((command instanceof ListCommand), is(true));
    }

    @Test
    public void createStatisticsCommand_ReturnsStatisticsCommand() {
        final IChatServerCommand command = commandFactory.createStatisticsCommand();

        assertThat((command instanceof StatisticsCommand), is(true));
    }

    @Test
    public void createQuitCommand_ReturnsQuitCommand() {
        final IChatServerCommand command = commandFactory.createQuitCommand();

        assertThat((command instanceof QuitCommand), is(true));
    }

    @Test
    public void createIdentityCommand_ReturnsExpectedIdentityCommand() {
        final String expectedUserName = "User Name";

        final IChatServerCommand command = commandFactory.createIdentityCommand(expectedUserName);

        assertThat((command instanceof IdentityCommand), is(true));

        final String actualUserName = ((IdentityCommand) command).getUserName();

        assertThat(actualUserName, is(equalTo(expectedUserName)));
    }

    @Test
    public void createBroadcastCommand_ReturnsExpectedBroadcastCommand() {
        final String expectedMessage = "Expected Message";

        final IChatServerCommand command = commandFactory.createBroadcastCommand(expectedMessage);

        assertThat((command instanceof BroadcastCommand), is(true));

        final String actualMessage = ((BroadcastCommand) command).getMessage();

        assertThat(actualMessage, is(equalTo(expectedMessage)));
    }

    @Test
    public void createPrivateMessageCommand_ReturnsExpectedPrivateMessageCommand() {
        final String expectedRecipient = "Recipient";
        final String expectedMessage = "Expected Message";

        final IChatServerCommand command = commandFactory.createPrivateMessageCommand(expectedRecipient, expectedMessage);

        assertThat((command instanceof PrivateMessageCommand), is(true));

        final String actualRecipient = ((PrivateMessageCommand) command).getRecipient();
        final String actualMessage = ((PrivateMessageCommand) command).getMessage();

        assertThat(actualRecipient, is(equalTo(expectedRecipient)));
        assertThat(actualMessage, is(equalTo(actualMessage)));
    }
}
