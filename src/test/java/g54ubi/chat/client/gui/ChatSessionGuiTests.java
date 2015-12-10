package g54ubi.chat.client.gui;

import g54ubi.chat.client.IChatSession;
import g54ubi.chat.client.IChatSessionFactory;
import g54ubi.chat.common.IResourceReceivedListener;
import org.fest.swing.fixture.FrameFixture;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static g54ubi.chat.client.gui.ChatSessionGuiComponents.*;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public final class ChatSessionGuiTests {
    @Mock
    private IChatSessionFactory mockChatSessionFactory;
    @Mock
    private IChatSession mockChatSession;

    private FrameFixture sessionGui;
    private IResourceReceivedListener<String> severResponseReceivedListener;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        when(mockChatSessionFactory.create(any())).thenAnswer(invocation -> {
            severResponseReceivedListener = invocation.getArgumentAt(0, IResourceReceivedListener.class);
            return mockChatSession;
        });

        sessionGui = new FrameFixture(new ChatSessionGui(mockChatSessionFactory));
        sessionGui.show();
    }

    @Test
    public void startSessionButton_ShouldCleanUpCurrentChatSession() {
        sessionGui.button(BUTTON_START_SESSION).click();

        verify(mockChatSession).quit();
        verify(mockChatSession).stop();
    }

    @Test
    public void startSessionButton_StartsNewChatSession() {

    }

    @Test
    public void quitButton_ShouldCleanUpCurrentSession() {

    }

    @Test
    public void setUserNameButton_ShouldCallChatSessionSetUserName() {

    }

    @Test
    public void setUserNameButton_ShouldSetExpectedUserName() {

    }

    @Test
    public void broadcastMessageButton_ShouldCallChatSessionBroadcastMessage() {

    }

    @Test
    public void broadcastMessageButton_ShouldBroadcastExpectedMessage() {

    }

    @Test
    public void broadcastMessageButton_ShouldClearMessageInputTextField() {

    }

    @Test
    public void listCurrentUsersButton_ShouldCallChatSessionListCurrentUsers() {

    }

    @Test
    public void showSessionStatisticsButton_ShouldCallChatSessionGetSessionStatistics() {

    }

    @Test
    public void sendPrivateMessageButton_ShouldCallChatSessionSendPrivateMessage() {

    }

    @Test
    public void sendPrivateMessageButton_ShouldSendMessageToExpectedRecipient() {

    }

    @Test
    public void sendPrivateMessageButton_ShouldSendExpectedMessageToRecipient() {

    }

    @Test
    public void sendPrivateMessage_ShouldClearMessageInputTextField() {

    }

    @Test
    public void chatMessageWindow_WhenServerSendsMessage_ShouldUpdateWithExpectedMessage() {

    }

    @Test
    public void chatMessagesWindow_ShouldNotBeEditable() {

    }
}
