package g54ubi.chat.client;

import g54ubi.chat.client.commands.IChatServerCommand;
import g54ubi.chat.common.IChatClient;
import g54ubi.chat.common.IResourceListener;
import g54ubi.chat.common.IResourceReceivedListener;
import g54ubi.chat.server.IChatServer;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.AdditionalAnswers.returnsSecondArg;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

public final class ChatSessionTests {
    @Mock
    private IChatClient mockChatServer;
    @Mock
    private IChatServerCommand mockChatServerCommand;
    @Mock
    private IChatServerCommandFactory mockChatServerCommandFactory;
    @Mock
    private IResourceListener<String> mockServerResponseListener;

    private ChatSession chatSession;
    private IResourceReceivedListener<String> chatSessionServerResponseReceivedListener;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        doAnswer(invocation ->
                chatSessionServerResponseReceivedListener = invocation.getArgumentAt(0, IResourceReceivedListener.class))
                .when(mockServerResponseListener)
                .listen(any());

        when(mockChatServerCommandFactory.createListCommand()).thenReturn(mockChatServerCommand);
        when(mockChatServerCommandFactory.createStatisticsCommand()).thenReturn(mockChatServerCommand);
        when(mockChatServerCommandFactory.createQuitCommand()).thenReturn(mockChatServerCommand);
        when(mockChatServerCommandFactory.createIdentityCommand(anyString())).thenReturn(mockChatServerCommand);
        when(mockChatServerCommandFactory.createBroadcastCommand(anyString())).thenReturn(mockChatServerCommand);
        when(mockChatServerCommandFactory.createPrivateMessageCommand(anyString(), anyString())).thenReturn(mockChatServerCommand);

        chatSession = new ChatSession(mockChatServer, mockChatServerCommandFactory, mockServerResponseListener);
    }

    @Test
    public void start_StartsServerResponseListener() {
        chatSession.start();

        verify(mockServerResponseListener, times(1)).listen(any());
    }

    @Test
    public void stop_StopsServerResponseListener() {
        chatSession.stop();

        verify(mockServerResponseListener, times(1)).stop();
    }

    @Test
    public void listCurrentUsers_SendsExpectedListCommandToServer() {
        final String expectedListCommand = "Expected List Command";
        setMockCommand(expectedListCommand);

        chatSession.listCurrentUsers();

        assertSessionSentExpectedMessageToServer(expectedListCommand);
    }

    @Test
    public void getSessionStatistics_SendsExpectedStatisticsCommandToServer() {
        final String expectedStatCommand = "Expected Stat Command";
        setMockCommand(expectedStatCommand);

        chatSession.getSessionStatistics();

        assertSessionSentExpectedMessageToServer(expectedStatCommand);
    }

    @Test
    public void quit_SendsExpectedQuitCommandToServer() {
        final String expectedQuitCommand = "Expected Quit Command";
        setMockCommand(expectedQuitCommand);

        chatSession.quit();

        assertSessionSentExpectedMessageToServer(expectedQuitCommand);
    }

    @Test
    public void setUserName_SendsIdentityCommandToServer_WithExpectedUserName() {
        final String expectedUserName = "Expected User Name";
        when(mockChatServerCommandFactory.createIdentityCommand(anyString()))
                .thenAnswer(invocation -> {
                    setMockCommand(invocation.getArgumentAt(0, String.class));
                    return mockChatServerCommand;
                });

        chatSession.setUserName(expectedUserName);

        assertSessionSentExpectedMessageToServer(expectedUserName);
    }

    @Test
    public void broadcastMessage_SendsBroadcastCommandToServer_WithExpectedMessage() {
        final String expectedMessage = "Expected Message";
        when(mockChatServerCommandFactory.createBroadcastCommand(anyString()))
                .thenAnswer(invocation -> {
                    setMockCommand(invocation.getArgumentAt(0, String.class));
                    return mockChatServerCommand;
                });

        chatSession.broadcastMessage(expectedMessage);

        assertSessionSentExpectedMessageToServer(expectedMessage);
    }

    @Test
    public void sendPrivateMessage_SendsPrivateMessageCommandToServer_WithExpectedRecipient() {
        final String expectedRecipient = "Expected Recipient";
        when(mockChatServerCommandFactory.createPrivateMessageCommand(anyString(), anyString()))
                .thenAnswer(invocation -> {
                    setMockCommand(invocation.getArgumentAt(0, String.class));
                    return mockChatServerCommand;
                });

        chatSession.sendPrivateMessage(expectedRecipient, "");

        assertSessionSentExpectedMessageToServer(expectedRecipient);
    }

    @Test
    public void sendPrivateMessage_SendsPrivateMessageCommandToServer_WithExpectedMessage() {
        final String expectedMessage = "Expected Message";
        when(mockChatServerCommandFactory.createPrivateMessageCommand(anyString(), anyString()))
                .thenAnswer(invocation -> {
                    setMockCommand(invocation.getArgumentAt(1, String.class));
                    return mockChatServerCommand;
                });

        chatSession.sendPrivateMessage("", expectedMessage);

        assertSessionSentExpectedMessageToServer(expectedMessage);
    }

    @Test
    public void whenChatSessionReceivesServerResponse_AndResponseListenerHasBeenRegistered_SendExpectedResponseToRegisteredResponseListener() {
        final IResourceReceivedListener<String> mockResponseReceivedListener = mock(IResourceReceivedListener.class);
        chatSession.registerResponseListener(mockResponseReceivedListener);

        chatSession.start();

        final String expectedResponse = "Expected Response";
        chatSessionServerResponseReceivedListener.onResourceReceived(expectedResponse);

        verify(mockResponseReceivedListener, times(1)).onResourceReceived(expectedResponse);
    }

    @Test
    public void whenChatSessionReceivesServerResponse_AndResponseListenerHasNotBeenRegistered_DoesNotThrowNullPointerException() {
        chatSession.start();
        chatSessionServerResponseReceivedListener.onResourceReceived("");
    }

    private void setMockCommand(final String command) {
        when(mockChatServerCommand.asServerMessage()).thenReturn(command);
    }

    private void assertSessionSentExpectedMessageToServer(final String expectedMessage) {
        verify(mockChatServer, times(1)).sendMessage(expectedMessage);
    }
}
