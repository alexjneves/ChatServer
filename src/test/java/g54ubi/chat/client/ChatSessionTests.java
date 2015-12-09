package g54ubi.chat.client;

import g54ubi.chat.common.IChatClient;
import g54ubi.chat.common.IResourceListener;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public final class ChatSessionTests {
    @Mock
    private IChatClient mockChatClient;
    @Mock
    private IChatServerCommandFactory mockChatServerCommandFactory;
    @Mock
    private IResourceListener<String> mockServerResponseListener;

    private ChatSession chatSession;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        chatSession = new ChatSession(mockChatClient, mockChatServerCommandFactory, mockServerResponseListener);
    }

    @Test
    public void start_StartsServerResponseListener() {

    }

    @Test
    public void stop_StopsServerResponseListener() {

    }

    @Test
    public void listCurrentUsers_SendsExpectedListCommandToServer() {

    }

    @Test
    public void getSessionStatistics_SendsExpectedStatCommandToServer() {

    }

    @Test
    public void quit_SendsExpectedQuitCommandToServer() {

    }

    @Test
    public void setUserName_SendsIdenCommandToServer_WithExpectedUserName() {

    }

    @Test
    public void broadcastMessage_SendsHailCommandToServer_WithExpectedMessage() {

    }

    @Test
    public void sendPrivateMessage_SendsMesgCommandToServer_WithExpectedRecipient() {

    }

    @Test
    public void sendPrivateMessage_SendsMesgCommandToServer_WithExpectedMessage() {

    }

    @Test
    public void whenChatSessonReceivesServerResponse_SendResponseToRegisteredResponseListener() {

    }
}
