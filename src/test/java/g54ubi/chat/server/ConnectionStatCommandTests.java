package g54ubi.chat.server;

import org.junit.Before;
import org.junit.Test;

import static org.mockito.Mockito.when;

public final class ConnectionStatCommandTests extends ConnectionTestBase {
    private final static int EXPECTED_NUMBER_OF_USERS = 4;
    private final static String BASE_MESSAGE = "OK There are currently " + EXPECTED_NUMBER_OF_USERS + " user(s) on the server ";

    private int expectedMessageCount = 0;

    @Override
    @Before
    public void setUp() {
        super.setUp();

        when(mockChatServer.getNumberOfUsers()).thenReturn(EXPECTED_NUMBER_OF_USERS);
    }

    @Test
    public void statCommand_WhenUnregistered_ClientReceivesSuccessMessage() {
        initialiseUnregisteredConnection();

        sendCommand(STAT_COMMAND);

        assertChatClientReceivedSuccessMessage();
    }

    @Test
    public void statCommand_WhenUnregistered_ClientReceivesExpectedMessage() {
        initialiseUnregisteredConnection();

        sendCommand(STAT_COMMAND);

        final String expectedMessage = BASE_MESSAGE + "You have not logged in yet";

        assertChatClientReceivedExpectedMessage(expectedMessage);
    }

    @Test
    public void statCommand_WhenRegistered_ClientReceivesSuccessMessage() {
        sendCommand(STAT_COMMAND);

        assertChatClientReceivedSuccessMessage();
    }

    @Test
    public void statCommand_WhenRegistered_AndClientHasNotSentAnyMessages_ClientReceivesMessageWithExpectedMessageCount() {
        sendCommand(STAT_COMMAND);

        final String expectedMessage = BASE_MESSAGE + "You are logged in and have sent " + expectedMessageCount + " message(s)";

        assertChatClientReceivedExpectedMessage(expectedMessage);
    }

    @Test
    public void statCommand_WhenRegistered_AndClientHasSentMessages_ClientReceivesMessageWithExpectedMessageCount() {
        expectedMessageCount = 3;

        for (int i = 0; i < expectedMessageCount; ++i) {
            sendCommand(HAIL_COMMAND, VALID_MESSAGE);
        }

        sendCommand(STAT_COMMAND);

        final String expectedMessage = BASE_MESSAGE + "You are logged in and have sent " + expectedMessageCount + " message(s)";

        assertChatClientReceivedExpectedMessage(expectedMessage);
    }
}
