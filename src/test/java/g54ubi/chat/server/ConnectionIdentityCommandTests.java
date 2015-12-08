package g54ubi.chat.server;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.when;

public final class ConnectionIdentityCommandTests extends ConnectionTestBase {
    @Override
    @Before
    public void setUp() {
        super.setUp();
        initialiseUnregisteredConnection();
    }

    @Ignore
    public void identityCommand_WhenCommandIsLowercase_ClientReceivesSuccessMessage() {
        final String lowercaseIdentityCommand = IDENTITY_COMMAND.toLowerCase();

        sendCommand(lowercaseIdentityCommand, VALID_USER_NAME);

        assertChatClientReceivedSuccessMessage();
    }

    @Test
    public void identityCommand_WhenUnregistered_WithUserNameWithNoSpaces_SetsExpectedUserName() {
        final String expectedUserNameWithoutSpace = "User1";

        sendCommand(IDENTITY_COMMAND, expectedUserNameWithoutSpace);

        final String actualUserName = connection.getUserName();

        assertThat(actualUserName, is(expectedUserNameWithoutSpace));
    }

    @Ignore
    public void identityCommand_WhenUnregistered_WithUserNameWithTrailingWhitespace_SetsExpectedUserName() {
        final String expectedUserNameWithTrailingWhitespace = "User ";

        sendCommand(IDENTITY_COMMAND, expectedUserNameWithTrailingWhitespace);

        final String actualUserName = connection.getUserName();

        assertThat(actualUserName, is(expectedUserNameWithTrailingWhitespace));
    }

    @Ignore
    public void identityCommand_WhenUnregistered_WithUserNameWithLeadingWhitespace_SetsExpectedUserName() {
        final String expectedUserNameWithLeadingWhitespace = " User";

        sendCommand(IDENTITY_COMMAND, expectedUserNameWithLeadingWhitespace);

        final String actualUserName = connection.getUserName();

        assertThat(actualUserName, is(expectedUserNameWithLeadingWhitespace));
    }

    @Test
    public void identityCommand_WhenUnregistered_AndUserDoesNotExist_ClientReceivesSuccessMessage() {
        sendCommand(IDENTITY_COMMAND, VALID_USER_NAME);

        assertChatClientReceivedSuccessMessage();
    }

    @Test
    public void identityCommand_WhenUnregistered_AndUserDoesNotExist_StateChangesToRegistered() {
        sendCommand(IDENTITY_COMMAND, VALID_USER_NAME);

        final int expectedState = Connection.STATE_REGISTERED;
        final int actualState = connection.getState();

        assertThat(actualState, is(equalTo(expectedState)));
    }

    @Test
    public void identityCommand_WhenUnregistered_AndUserDoesExist_ClientReceivesErrorMessage() {
        when(mockChatServer.doesUserExist(VALID_USER_NAME)).thenReturn(true);

        sendCommand(IDENTITY_COMMAND, VALID_USER_NAME);

        assertChatClientReceivedErrorMessage();
    }

    @Test
    public void identityCommand_WhenAlreadyRegistered_ClientReceivesErrorMessage() {
        initialiseRegisteredConnection();

        assertThat(connection.getState(), is(equalTo(Connection.STATE_REGISTERED)));

        sendCommand(IDENTITY_COMMAND, VALID_USER_NAME);

        assertChatClientReceivedErrorMessage();
    }
}
