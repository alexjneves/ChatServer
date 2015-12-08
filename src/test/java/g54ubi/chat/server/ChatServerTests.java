package g54ubi.chat.server;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

public final class ChatServerTests {
    @Mock
    private IConnectionListenerFactory mockConnectionListenerFactory;
    @Mock
    private IResourceListener<IConnection> mockConnectionListener;

    private ChatServer chatServer;
    private IResourceReceivedListener<IConnection> connectionReceivedListener;

    private ArrayList<IConnection> expectedConnections;
    private ArrayList<String> expectedUserNames;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        when(mockConnectionListenerFactory.create(any())).thenReturn(mockConnectionListener);

        doAnswer(invocation -> connectionReceivedListener = invocation.getArgumentAt(0, IResourceReceivedListener.class))
                .when(mockConnectionListener)
                .listen(any());

        initialiseChatServerWithRegisteredClients(3);
        assertThat(connectionReceivedListener, is(notNullValue()));
    }

    private void initialiseChatServerWithRegisteredClients(final int numberOfClients) {
        chatServer = new ChatServer(mockConnectionListenerFactory);
        chatServer.start();

        expectedConnections = new ArrayList<>(numberOfClients);
        expectedUserNames = new ArrayList<>(numberOfClients);

        for (int i = 0; i < numberOfClients; ++i) {
            final String userName = "User" + i;
            final IConnection connection = createRegisteredConnection(userName);

            expectedConnections.add(connection);
            expectedUserNames.add(userName);
            connectionReceivedListener.onResourceReceived(connection);
        }
    }

    @Test
    public void start_StartsConnectionListener() {
        final IResourceListener<IConnection> connectionListener = mock(IResourceListener.class);
        when(mockConnectionListenerFactory.create(any())).thenReturn(connectionListener);

        chatServer = new ChatServer(mockConnectionListenerFactory);
        chatServer.start();

        verify(connectionListener).listen(any());
    }

    @Test
    public void getUserList_WhenNoUsersAreConnected_ReturnsEmptyList() {
        initialiseChatServerWithRegisteredClients(0);

        final ArrayList<String> emptyList = new ArrayList<>();
        final ArrayList<String> actualList = chatServer.getUserList();

        assertThat(actualList, is(equalTo(emptyList)));
    }

    @Test
    public void getUserList_WhenOneUserIsConnected_ReturnsListWithExpectedUser() {
        initialiseChatServerWithRegisteredClients(1);

        final ArrayList<String> actualUserList = chatServer.getUserList();

        assertThat(actualUserList, is(equalTo(expectedUserNames)));
    }

    @Test
    public void getUserList_WhenMultipleUsersAreConnected_ReturnsListWithExpectedUsers() {
        initialiseChatServerWithRegisteredClients(3);

        final ArrayList<String> actualUserList = chatServer.getUserList();

        assertThat(actualUserList, is(equalTo(expectedUserNames)));
    }

    @Test
    public void getUserList_WhenUserIsNotRegistered_DoesNotIncludeUserInList() {
        final String unregisteredUser = "UnregisteredUser";
        final IConnection unregisteredConnection = createUnregisteredConnection(unregisteredUser);

        connectionReceivedListener.onResourceReceived(unregisteredConnection);

        final ArrayList<String> actualUserList = chatServer.getUserList();

        assertThat(actualUserList.contains(unregisteredUser), is(false));
    }

    @Ignore
    public void doesUserExist_WhenUserIsNull_ReturnsFalse() {
        final String nullUser = null;

        final boolean userExists = chatServer.doesUserExist(nullUser);

        assertThat(userExists, is(false));
    }

    @Test
    public void doesUserExist_WhenUserDoesExist_AndIsLastInTheList_ReturnsTrue() {
        final int lastUserIndex = expectedUserNames.size() - 1;
        final String lastExistingUser = expectedUserNames.get(lastUserIndex);

        final boolean userExists = chatServer.doesUserExist(lastExistingUser);

        assertThat(userExists, is(true));
    }

    @Ignore
    public void doesUserExist_WhenUserDoesExist_AndIsFirstInTheList_ReturnsTrue() {
        final int firstUserIndex = 0;
        final String firstExistingUser = expectedUserNames.get(firstUserIndex);

        final boolean userExists = chatServer.doesUserExist(firstExistingUser);

        assertThat(userExists, is(true));
    }

    @Ignore
    public void doesUserExist_WhenUserDoesExist_AndIsInTheMiddleOfTheList_ReturnsTrue() {
        final int numberOfClients = 4;
        final int middleUserIndex = numberOfClients / 2;

        initialiseChatServerWithRegisteredClients(numberOfClients);

        final String middleExistingUser = expectedUserNames.get(middleUserIndex);

        final boolean userExists = chatServer.doesUserExist(middleExistingUser);

        assertThat(userExists, is(true));
    }

    @Test
    public void doesUserExist_WhenUserDoesNotExist_ReturnsFalse() {
        final String nonExistingUser = "DoesNotExist";

        final boolean userExists = chatServer.doesUserExist(nonExistingUser);

        assertThat(userExists, is(false));
    }

    @Test
    public void doesUserExist_WhenUserDoesExist_ButIsUnregistered_ReturnsFalse() {
        final String unregisteredUser = "UnregisteredUser";
        final IConnection unregisteredConnection = createUnregisteredConnection(unregisteredUser);

        connectionReceivedListener.onResourceReceived(unregisteredConnection);

        final boolean userExists = chatServer.doesUserExist(unregisteredUser);

        assertThat(userExists, is(false));
    }

    @Ignore
    public void broadcastMessage_WithNullMessage_DoesNotSendMessageToClients() {
        final String nullMessage = null;

        chatServer.broadcastMessage(nullMessage);

        for (final IConnection connection : expectedConnections) {
            verify(connection, times(0)).messageForConnection(anyString());
        }
    }

    @Test
    public void broadcastMessage_WithValidMessage_SendsExpectedMessageToAllClients() {
        final String expectedMessage = "Expected Message";

        chatServer.broadcastMessage(expectedMessage);

        for (final IConnection connection : expectedConnections) {
            verify(connection, times(1)).messageForConnection(expectedMessage + System.lineSeparator());
        }
    }

    @Ignore
    public void sendPrivateMessage_WithNullMessage_AndRecipientExists_ReturnsFalse() {
        final String nullMessage = null;
        final String existingRecipient = expectedUserNames.get(0);

        final boolean sendResult = chatServer.sendPrivateMessage(nullMessage, existingRecipient);

        assertThat(sendResult, is(false));
    }

    @Ignore
    public void sendPrivateMessage_WithNullMessage_AndRecipientExists_DoesNotSendMessageToRecipient() {
        final String nullMessage = null;
        final String existingRecipient = expectedUserNames.get(0);
        final IConnection existingConnection = expectedConnections.get(0);

        chatServer.sendPrivateMessage(nullMessage, existingRecipient);

        verify(existingConnection, times(0)).messageForConnection(anyString());
    }

    @Test
    public void sendPrivateMessage_WhenRecipientIsNull_ReturnsFalse() {
        final String nullRecipient = null;

        final boolean sendResult = chatServer.sendPrivateMessage("", nullRecipient);

        assertThat(sendResult, is(false));
    }

    @Test
    public void sendPrivateMessage_WhenRecipientDoesNotExist_ReturnsFalse() {
        final String nonExistentRecipient = "NonExistentRecipient";

        final boolean sendResult = chatServer.sendPrivateMessage("", nonExistentRecipient);

        assertThat(sendResult, is(false));
    }

    @Test
    public void sendPrivateMessage_WhenRecipientDoesNotExist_DoesNotSendMessageToAnyClients() {
        final String nonExistentRecipient = "NonExistentRecipient";

        chatServer.sendPrivateMessage("", nonExistentRecipient);

        for (final IConnection connection : expectedConnections) {
            verify(connection, times(0)).messageForConnection(anyString());
        }
    }

    @Test
    public void sendPrivateMessage_WhenRecipientDoesExist_ReturnsTrue() {
        final String existingRecipient = getLastExistingUserName();

        final boolean sendResult = chatServer.sendPrivateMessage("", existingRecipient);

        assertThat(sendResult, is(true));
    }

    @Test
    public void sendPrivateMessage_WhenRecipientDoesExist_SendsExpectedMessageToRecipient() {
        final int expectedUserIndex = expectedUserNames.size() - 1;

        final String expectedMessage = "ExpectedMessage";
        final IConnection expectedConnection = expectedConnections.get(expectedUserIndex);
        final String existingRecipient = expectedUserNames.get(expectedUserIndex);

        chatServer.sendPrivateMessage(expectedMessage, existingRecipient);

        verify(expectedConnection, times(1)).messageForConnection(expectedMessage + System.lineSeparator());
    }

    @Test
    public void sendPrivateMessage_WhenRecipientDoesExist_DoesNotSendMessageToOtherClients() {
        final String existingRecipient = getLastExistingUserName();

        chatServer.sendPrivateMessage("", existingRecipient);

        for (final IConnection connection : expectedConnections) {
            if (!connection.getUserName().equals(existingRecipient)) {
                verify(connection, times(0)).messageForConnection(anyString());
            }
        }
    }

    @Test
    public void sendPrivateMessage_WhenRecipientDoesExist_ButIsUnregistered_ReturnsFalse() {
        final String existingUnregisteredRecipient = "UnregisteredRecipient";

        final IConnection unregisteredConnection = createUnregisteredConnection(existingUnregisteredRecipient);
        connectionReceivedListener.onResourceReceived(unregisteredConnection);

        final boolean sendResult = chatServer.sendPrivateMessage("", existingUnregisteredRecipient);

        assertThat(sendResult, is(false));
    }

    @Test
    public void removeDeadUsers_RemovesAllClientsWhichAreNotRunning() {
        initialiseChatServerWithRegisteredClients(3);

        final int lastUserIndex = expectedUserNames.size() - 1;

        for (final IConnection connection : expectedConnections) {
            when(connection.isRunning()).thenReturn(false);
        }

        final IConnection runningConnection = expectedConnections.get(lastUserIndex);
        final String runningConnectionUserName = expectedUserNames.get(lastUserIndex);

        when(runningConnection.isRunning()).thenReturn(true);

        final ArrayList<String> expectedUsersAfterRemoval = new ArrayList<String>(1) {{ add(runningConnectionUserName); }};

        chatServer.removeDeadUsers();

        final ArrayList<String> actualUsersAfterRemoval = chatServer.getUserList();

        assertThat(actualUsersAfterRemoval, is(equalTo(expectedUsersAfterRemoval)));
    }

    @Test
    public void removeDeadUsers_DoesNotRemoveClientsWhichAreRunning() {
        final ArrayList<String> usersBeforeRemoval = expectedUserNames;

        for (final IConnection connection : expectedConnections) {
            when(connection.isRunning()).thenReturn(true);
        }

        chatServer.removeDeadUsers();

        final ArrayList<String> usersAfterRemoval = chatServer.getUserList();

        assertThat(usersAfterRemoval, is(equalTo(usersBeforeRemoval)));
    }

    @Test
    public void getNumberOfUsers_WhenThereAreNoUsers_ReturnsZero() {
        final int zeroUsers = 0;

        initialiseChatServerWithRegisteredClients(zeroUsers);

        final int actualNumberOfUsers = chatServer.getNumberOfUsers();

        assertThat(actualNumberOfUsers, is(zeroUsers));
    }

    @Test
    public void getNumberOfUsers_WhenThereAreUsers_ReturnsExpectedNumberOfUsers() {
        final int expectedNumberOfUsers = 5;

        initialiseChatServerWithRegisteredClients(expectedNumberOfUsers);

        final int actualNumberOfUsers = chatServer.getNumberOfUsers();

        assertThat(actualNumberOfUsers, is(expectedNumberOfUsers));
    }

    private String getLastExistingUserName() {
        return expectedUserNames.get(expectedUserNames.size() - 1);
    }

    private IConnection createRegisteredConnection(final String userName) {
        return createConnection(userName, true);
    }

    private IConnection createUnregisteredConnection(final String userName) {
        return createConnection(userName, false);
    }

    private IConnection createConnection(final String userName, final boolean registered) {
        final int state = registered ? Connection.STATE_REGISTERED : Connection.STATE_UNREGISTERED;

        final IConnection connection = mock(IConnection.class);

        when(connection.getUserName()).thenReturn(userName);
        when(connection.getState()).thenReturn(state);

        return connection;
    }
}
