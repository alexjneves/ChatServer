package g54ubi.chat.server;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import java.util.ArrayList;

import static org.mockito.Mockito.when;

public final class ConnectionListCommandTests extends ConnectionTestBase {
    private ArrayList<String> userList;

    @Override
    @Before
    public void setUp() {
        super.setUp();

        userList = new ArrayList<>();
        when(mockChatServer.getUserList()).thenReturn(userList);
    }

    @Ignore
    public void listCommand_WhenCommandIsLowercase_ClientReceivesSuccessMessage() {
        final String lowercaseListCommand = LIST_COMMAND.toLowerCase();

        sendCommand(lowercaseListCommand);

        assertChatClientReceivedSuccessMessage();
    }

    @Test
    public void listCommand_WhenUnregistered_ClientReceivesErrorMessage() {
        initialiseUnregisteredConnection();

        sendCommand(LIST_COMMAND);

        assertChatClientReceivedErrorMessage();
    }

    @Test
    public void listCommand_WhenRegistered_ClientReceivesSuccessMessage() {
        sendCommand(LIST_COMMAND);

        assertChatClientReceivedSuccessMessage();
    }

    @Test
    public void listCommand_WhenRegistered_AndNoClientsAreConnected_ClientReceivesExpectedUserList() {
        final String expectedMessage = "OK ";

        sendCommand(LIST_COMMAND);

        assertChatClientReceivedExpectedMessage(expectedMessage);
    }

    @Test
    public void listCommand_WhenRegistered_AndOneClientIsConnected_ClientReceivesExpectedUserList() {
        userList.add("User1");

        final String expectedMessage = "OK User1, ";

        sendCommand(LIST_COMMAND);

        assertChatClientReceivedExpectedMessage(expectedMessage);
    }

    @Test
    public void listCommand_WhenRegistered_AndTwoClientsAreConnected_ClientReceivesExpectedUserList() {
        userList.add("User1");
        userList.add("User2");

        final String expectedMessage = "OK User1, User2, ";

        sendCommand(LIST_COMMAND);

        assertChatClientReceivedExpectedMessage(expectedMessage);
    }
}
