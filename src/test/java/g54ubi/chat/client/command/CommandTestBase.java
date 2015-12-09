package g54ubi.chat.client.command;

import g54ubi.chat.client.commands.IChatServerCommand;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class CommandTestBase {

    protected void assertCommandFormsExpectedMessage(final IChatServerCommand command, final String expectedMessage) {
        final String actualMessage = command.asServerMessage();

        assertThat(actualMessage, is(expectedMessage));
    }
}
