package g54ubi.chat.client.gui;

import g54ubi.chat.client.IChatSession;
import g54ubi.chat.client.IChatSessionFactory;
import g54ubi.chat.common.IResourceReceivedListener;
import org.fest.swing.fixture.FrameFixture;
import org.fest.swing.fixture.JTextComponentFixture;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static g54ubi.chat.client.gui.ChatSessionGuiComponents.*;
import static org.mockito.Matchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.internal.verification.VerificationModeFactory.times;

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

    @After
    public void tearDown() {
        sessionGui.cleanUp();
    }

    @Test
    public void startSessionButton_ShouldCleanUpCurrentChatSession() {
        sessionGui.button(BUTTON_START_SESSION).click();

        verify(mockChatSession).quit();
        verify(mockChatSession).stop();
    }

    @Test
    public void startSessionButton_StartsNewChatSession() {
        sessionGui.button(BUTTON_START_SESSION).click();

        verify(mockChatSessionFactory, times(2)).create(any());
    }

    @Test
    public void quitButton_ShouldCleanUpCurrentSession() {
        sessionGui.button(BUTTON_QUIT).click();

        verify(mockChatSession).quit();
        verify(mockChatSession).stop();
    }

    @Test
    public void setUserNameButton_ShouldCallChatSessionSetUserName() {
        sessionGui.button(BUTTON_SET_USER_NAME).click();

        verify(mockChatSession).setUserName(anyString());
    }

    @Test
    public void setUserNameButton_ShouldSetExpectedUserName() {
        final String expectedUserName = "Expected User Name";
        sessionGui.textBox(TEXT_FIELD_USER_NAME).setText(expectedUserName);

        sessionGui.button(BUTTON_SET_USER_NAME).click();

        verify(mockChatSession).setUserName(expectedUserName);
    }

    @Test
    public void broadcastMessageButton_ShouldCallChatSessionBroadcastMessage() {
        sessionGui.button(BUTTON_BROADCAST_MESSAGE).click();

        verify(mockChatSession).broadcastMessage(anyString());
    }

    @Test
    public void broadcastMessageButton_ShouldBroadcastExpectedMessage() {
        final String expectedMessage = "Expected Message";
        sessionGui.textBox(TEXT_FIELD_MESSAGE).setText(expectedMessage);

        sessionGui.button(BUTTON_BROADCAST_MESSAGE).click();

        verify(mockChatSession).broadcastMessage(expectedMessage);
    }

    @Test
    public void broadcastMessageButton_ShouldClearMessageInputTextField() {
        final JTextComponentFixture messageTextField = sessionGui.textBox(TEXT_FIELD_MESSAGE);

        final String nonEmptyString = "Non Empty String";
        messageTextField.setText(nonEmptyString);

        sessionGui.button(BUTTON_SEND_PRIVATE_MESSAGE).click();

        messageTextField.requireEmpty();
    }

    @Test
    public void listCurrentUsersButton_ShouldCallChatSessionListCurrentUsers() {
        sessionGui.button(BUTTON_LIST_CURRENT_USERS).click();

        verify(mockChatSession).listCurrentUsers();
    }

    @Test
    public void showSessionStatisticsButton_ShouldCallChatSessionGetSessionStatistics() {
        sessionGui.button(BUTTON_SHOW_STATISTICS).click();

        verify(mockChatSession).getSessionStatistics();
    }

    @Test
    public void sendPrivateMessageButton_ShouldCallChatSessionSendPrivateMessage() {
        sessionGui.button(BUTTON_SEND_PRIVATE_MESSAGE).click();

        verify(mockChatSession).sendPrivateMessage(anyString(), anyString());
    }

    @Test
    public void sendPrivateMessageButton_ShouldSendMessageToExpectedRecipient() {
        final String expectedRecipient = "Expected Recipient";
        sessionGui.textBox(TEXT_FIELD_PRIVATE_MESSAGE_RECIPIENT).setText(expectedRecipient);

        sessionGui.button(BUTTON_SEND_PRIVATE_MESSAGE).click();

        verify(mockChatSession).sendPrivateMessage(eq(expectedRecipient), anyString());
    }

    @Test
    public void sendPrivateMessageButton_ShouldSendExpectedMessageToRecipient() {
        final String expectedMessage = "Expected Message";
        sessionGui.textBox(TEXT_FIELD_MESSAGE).setText(expectedMessage);

        sessionGui.button(BUTTON_SEND_PRIVATE_MESSAGE).click();

        verify(mockChatSession).sendPrivateMessage(anyString(), eq(expectedMessage));
    }

    @Test
    public void sendPrivateMessage_ShouldClearMessageInputTextField() {
        final JTextComponentFixture messageTextField = sessionGui.textBox(TEXT_FIELD_MESSAGE);

        final String nonEmptyString = "Non Empty String";
        messageTextField.setText(nonEmptyString);

        sessionGui.button(BUTTON_SEND_PRIVATE_MESSAGE).click();

        messageTextField.requireEmpty();
    }

    @Test
    public void chatMessageWindow_WhenServerSendsMessage_ShouldUpdateWithExpectedMessageAndLineSeparator() {
        final String message = "Message";
        severResponseReceivedListener.onResourceReceived(message);

        final String expectedChatMessageWindowContents = message + System.lineSeparator();

        sessionGui.textBox(TEXT_AREA_CHAT_MESSAGES).requireText(expectedChatMessageWindowContents);
    }

    @Test
    public void chatMessageWindow_WhenServerSendsMessage_ShouldAppendExpectedMessageToExistingContents() {
        final JTextComponentFixture chatMessageWindow = sessionGui.textBox(TEXT_AREA_CHAT_MESSAGES);

        final String existingContents = "Message 1\nMessage2\n";
        chatMessageWindow.setText(existingContents);

        final String message = "Message";
        severResponseReceivedListener.onResourceReceived(message);

        final String expectedChatMessageWindowContents = existingContents + message + System.lineSeparator();

        sessionGui.textBox(TEXT_AREA_CHAT_MESSAGES).requireText(expectedChatMessageWindowContents);
    }

    @Test
    public void chatMessagesWindow_ShouldNotBeEditable() {
        sessionGui.textBox(TEXT_AREA_CHAT_MESSAGES).requireNotEditable();
    }
}
