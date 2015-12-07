package g54ubi.chat.server;

import org.junit.Before;
import org.junit.Test;

import static org.mockito.Mockito.when;

public final class ConnectionStatCommandTests extends ConnectionTestBase {
    private final static int EXPECTED_NUMBER_OF_USERS = 4;
    private final static String BASE_MESSAGE = "OK There are currently " + EXPECTED_NUMBER_OF_USERS + " user(s) on the server ";

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
        final int expectedMessageCount = 0;

        sendCommand(STAT_COMMAND);

        final String expectedMessage = BASE_MESSAGE + "You are logged in and have sent " + expectedMessageCount + " message(s)";

        assertChatClientReceivedExpectedMessage(expectedMessage);
    }

    @Test
    public void statCommand_WhenRegistered_AndClientHasSentMessages_ClientReceivesMessageWithExpectedMessageCount() {
        final int expectedMessageCount = 3;
        sendMessage(VALID_MESSAGE, expectedMessageCount);

        sendCommand(STAT_COMMAND);

        final String expectedMessage = BASE_MESSAGE + "You are logged in and have sent " + expectedMessageCount + " message(s)";

        assertChatClientReceivedExpectedMessage(expectedMessage);
    }
}
