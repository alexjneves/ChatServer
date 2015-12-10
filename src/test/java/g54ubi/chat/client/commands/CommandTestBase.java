package g54ubi.chat.client.commands;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public abstract class CommandTestBase {
    protected void assertCommandFormsExpectedMessage() {
        final String expectedMessage = getExpectedMessage();
        final String actualMessage = createCommand().formMessage();

        assertThat(actualMessage, is(expectedMessage));
    }

    protected abstract IChatServerCommand createCommand();
    protected abstract String getExpectedMessage();
}
