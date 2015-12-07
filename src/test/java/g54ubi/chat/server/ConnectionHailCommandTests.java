package g54ubi.chat.server;

import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.*;

public final class ConnectionHailCommandTests extends ConnectionTestBase {
    private String broadcastMessage;

    @Override
    @Before
    public void setUp() {
        super.setUp();

        doAnswer(invocation -> broadcastMessage = invocation.getArgumentAt(0, String.class))
                .when(mockChatServer)
                .broadcastMessage(anyString());
    }

    @Test
    public void hailCommand_WhenUnregistered_ClientReceivesErrorMessage() {
        initialiseUnregisteredConnection();

        sendCommand(HAIL_COMMAND, VALID_MESSAGE);

        assertChatClientReceivedErrorMessage();
    }

    @Test
    public void hailCommand_WhenRegistered_MessageIsBroadcast() {
        sendCommand(HAIL_COMMAND, VALID_MESSAGE);

        verify(mockChatServer, times(1)).broadcastMessage(anyString());
    }

    @Test
    public void hailCommand_WhenRegistered_MessageIsBroadcastWithExpectedUserName() {
        sendCommand(HAIL_COMMAND, VALID_MESSAGE);

        assertThat(broadcastMessage.contains(VALID_USER_NAME), is(true));
    }

    @Test
    public void hailCommand_WhenRegistered_MessageIsBroadcastWithExpectedContents() {
        final String expectedMessageContents = "This is my expected message contents";

        sendCommand(HAIL_COMMAND, expectedMessageContents);

        assertThat(broadcastMessage.contains(expectedMessageContents), is(true));
    }
}
