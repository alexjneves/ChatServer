package g54ubi.chat.server;

import org.junit.Before;
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

        initialiseChatServerWithClients(0);
    }

    private void initialiseChatServerWithClients(final int numberOfClients) {
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
        chatServer = new ChatServer(mockConnectionListenerFactory);
        chatServer.start();

        assertThat(connectionReceivedListener, is(notNullValue()));
    }

    @Test
    public void getUserList_WhenNoUsersAreConnected_ReturnsEmptyList() {
        final ArrayList<String> emptyList = new ArrayList<>();

        final ArrayList<String> actualList = chatServer.getUserList();

        assertThat(actualList, is(equalTo(emptyList)));
    }

    @Test
    public void getUserList_WhenOneUserIsConnected_ReturnsListWithExpectedUser() {
        initialiseChatServerWithClients(1);

        final ArrayList<String> actualUserList = chatServer.getUserList();

        assertThat(actualUserList, is(equalTo(expectedUserNames)));
    }

    @Test
    public void getUserList_WhenMultipleUsersAreConnected_ReturnsListWithExpectedUsers() {
        initialiseChatServerWithClients(3);

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

    @Test
    public void doesUserExist_WhenUserDoesExist_ReturnsTrue() {
        initialiseChatServerWithClients(1);
        final String existingUser = expectedUserNames.get(0);

        final boolean userExists = chatServer.doesUserExist(existingUser);

        assertThat(userExists, is(true));
    }

    @Test
    public void doesUserExist_WhenUserDoesNotExist_ReturnsFalse() {
        initialiseChatServerWithClients(1);
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

    @Test
    public void getNumberOfUsers_WhenThereAreUsers_ReturnsExpectedNumberOfUsers() {
        final int expectedNumberOfUsers = 5;

        initialiseChatServerWithClients(expectedNumberOfUsers);

        final int actualNumberOfUsers = chatServer.getNumberOfUsers();

        assertThat(actualNumberOfUsers, is(expectedNumberOfUsers));
    }

    @Test
    public void broadcastMessage_SendsExpectedMessageToAllClients() {
        final String expectedMessage = "Expected Message";

        initialiseChatServerWithClients(3);

        chatServer.broadcastMessage(expectedMessage);

        for (final IConnection connection : expectedConnections) {
            verify(connection, times(1)).messageForConnection(expectedMessage + System.lineSeparator());
        }
    }

    @Test
    public void sendPrivateMessage_WhenRecipientDoesNotExist_ReturnsFalse() {

    }

    @Test
    public void sendPrivateMessage_WhenRecipientDoesNotExist_DoesNotSendMessageToAnyClients() {

    }

    @Test
    public void sendPrivateMessage_WhenRecipientDoesExist_ReturnsTrue() {

    }

    @Test
    public void sendPrivateMessage_WhenRecipientDoesExist_SendsExpectedMessageToRecipient() {

    }

    @Test
    public void sendPrivateMessage_WhenRecipientDoesExist_ButIsUnregistered_ReturnsFalse() {

    }

    @Test
    public void removeDeadUsers_RemovesAllClientsWhichAreNotRunning() {

    }

    @Test
    public void removeDeadUsers_DoesNotRemoveClientsWhichAreRunning() {

    }

    @Test
    public void getNumberOfUsers_WhenThereAreNoUsers_ReturnsZero() {
        final int zeroUsers = 0;

        initialiseChatServerWithClients(zeroUsers);

        final int actualNumberOfUsers = chatServer.getNumberOfUsers();

        assertThat(actualNumberOfUsers, is(zeroUsers));
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
