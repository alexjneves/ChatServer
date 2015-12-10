package g54ubi.chat.client.gui;

import g54ubi.chat.client.IChatSession;
import g54ubi.chat.client.IChatSessionFactory;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import static g54ubi.chat.client.gui.ChatSessionGuiComponents.*;

public final class ChatSessionGui extends JFrame implements WindowListener {
    private static final int INITIAL_FRAME_WIDTH = 800;
    private static final int INITIAL_FRAME_HEIGHT = 800;

    private final IChatSessionFactory chatSessionFactory;
    private final JTextArea chatMessagesWindow;
    private final JTextField messageInputTextField;
    private final JTextField userNameInputTextField;
    private final JTextField privateMessageRecipientInputTextField;

    private IChatSession chatSession;

    public ChatSessionGui(final IChatSessionFactory chatSessionFactory) {
        super();
        this.chatSessionFactory = chatSessionFactory;

        addWindowListener(this);

        this.chatMessagesWindow = createChatMessagesWindow();
        this.messageInputTextField = createMessageInputTextField();
        this.userNameInputTextField = createUserNameInputTextField();
        this.privateMessageRecipientInputTextField = createPrivateMessageRecipientInputTextField();

        this.chatSession = this.chatSessionFactory.create(this::onServerResponseReceived);

        initialiseComponents();
    }

    private void initialiseComponents() {
        setSize(INITIAL_FRAME_WIDTH, INITIAL_FRAME_HEIGHT);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        final JPanel panel = createPanel();
        this.add(panel);

        // ROW 1
        panel.add(createStartSessionButton(), createConstraints(0, 0, 2));
        panel.add(createQuitButton(), createConstraints(0, 2));

        // ROW 2
        panel.add(createUserNameInputTextFieldLabel(), createConstraints(1, 0));
        panel.add(userNameInputTextField, createConstraints(1, 1));
        panel.add(createSetUserNameButton(), createConstraints(1, 2));

        // ROW 3
        panel.add(createBroadcastMessageButton(), createConstraints(2, 0));
        panel.add(createListCurrentUsersButton(), createConstraints(2, 1));
        panel.add(createShowSessionStatisticsButton(), createConstraints(2, 2));

        // ROW 4
        panel.add(createPrivateMessageRecipientInputTextFieldLabel(), createConstraints(3, 0));
        panel.add(privateMessageRecipientInputTextField, createConstraints(3, 1));
        panel.add(createSendPrivateMessageButton(), createConstraints(3, 2));

        // ROW 5
        final GridBagConstraints c = createConstraints(4, 0, 3);
        c.weighty = 1;
        c.fill = GridBagConstraints.BOTH;
        panel.add(createChatTextWindowScrollPane(this.chatMessagesWindow), c);

        // ROW 6
        panel.add(messageInputTextField, createConstraints(5, 0, 3));
    }

    private JTextArea createChatMessagesWindow() {
        final JTextArea chatMessageWindow = new JTextArea();

        chatMessageWindow.setLineWrap(true);
        chatMessageWindow.setName(TEXT_AREA_CHAT_MESSAGES);

        return chatMessageWindow;
    }

    private JTextField createMessageInputTextField() {
        final JTextField messageInputTextField = new JTextField();

        messageInputTextField.setMaximumSize(new Dimension(Integer.MAX_VALUE, messageInputTextField.getPreferredSize().height));
        messageInputTextField.setName(ChatSessionGuiComponents.TEXT_FIELD_MESSAGE);

        return messageInputTextField;
    }

    private JTextField createUserNameInputTextField() {
        final JTextField userNameInputTextField = new JTextField(10);

        userNameInputTextField.setMaximumSize(new Dimension(userNameInputTextField.getPreferredSize().width, userNameInputTextField.getPreferredSize().height));
        userNameInputTextField.setName(ChatSessionGuiComponents.TEXT_FIELD_USER_NAME);

        return userNameInputTextField;
    }

    private JTextField createPrivateMessageRecipientInputTextField() {
        final JTextField privateMessageRecipientInputTextField = new JTextField();

        privateMessageRecipientInputTextField.setMaximumSize(new Dimension(Integer.MAX_VALUE, privateMessageRecipientInputTextField.getPreferredSize().height));
        privateMessageRecipientInputTextField.setName(ChatSessionGuiComponents.TEXT_FIELD_PRIVATE_MESSAGE_RECIPIENT);

        return privateMessageRecipientInputTextField;
    }

    private JPanel createPanel() {
        final JPanel panel = new JPanel();
        panel.setLayout(new GridBagLayout());

        return panel;
    }

    private GridBagConstraints createConstraints(final int row, final int column) {
        final GridBagConstraints constraints = new GridBagConstraints();

        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.gridy = row;
        constraints.gridx = column;
        constraints.weightx = 1;

        return constraints;
    }

    private GridBagConstraints createConstraints(final int row, final int column, final int gridWidth) {
        final GridBagConstraints constraints = createConstraints(row, column);
        constraints.gridwidth = gridWidth;

        return constraints;
    }

    private JButton createStartSessionButton() {
        final JButton startSessionButton = new JButton("Start Session");

        startSessionButton.setName(ChatSessionGuiComponents.BUTTON_START_SESSION);

        startSessionButton.addActionListener(actionListener -> {
            endSession();
            chatSession = chatSessionFactory.create(this::onServerResponseReceived);
            chatSession.start();
        });

        return startSessionButton;
    }

    private JLabel createUserNameInputTextFieldLabel() {
        final JLabel userNameInputTextFieldLabel = new JLabel("User Name:");

        userNameInputTextFieldLabel.setLabelFor(userNameInputTextField);
        userNameInputTextFieldLabel.setName(ChatSessionGuiComponents.LABEL_USER_NAME);

        return userNameInputTextFieldLabel;
    }

    private JButton createListCurrentUsersButton() {
        final JButton listCurrentUsersButton = new JButton("List Current Users");

        listCurrentUsersButton.addActionListener(actionListener -> chatSession.listCurrentUsers());
        listCurrentUsersButton.setName(ChatSessionGuiComponents.BUTTON_LIST_CURRENT_USERS);

        return listCurrentUsersButton;
    }

    private JButton createShowSessionStatisticsButton() {
        final JButton showSessionStatisticsButton = new JButton("Show Session Statistics");

        showSessionStatisticsButton.addActionListener(actionListener -> chatSession.getSessionStatistics());
        showSessionStatisticsButton.setName(ChatSessionGuiComponents.BUTTON_SHOW_STATISTICS);

        return showSessionStatisticsButton;
    }

    private JLabel createPrivateMessageRecipientInputTextFieldLabel() {
        final JLabel privateMessageRecipientInputTextFieldLabel = new JLabel("Private Message Recipient:");

        privateMessageRecipientInputTextFieldLabel.setLabelFor(privateMessageRecipientInputTextField);
        privateMessageRecipientInputTextFieldLabel.setName(ChatSessionGuiComponents.LABEL_PRIVATE_MESSAGE_RECIPIENT);

        return privateMessageRecipientInputTextFieldLabel;
    }

    private JButton createSendPrivateMessageButton() {
        final JButton sendPrivateMessageButton = new JButton("Send Private Message");

        sendPrivateMessageButton.setName(ChatSessionGuiComponents.BUTTON_SEND_PRIVATE_MESSAGE);

        sendPrivateMessageButton.addActionListener(actionListener -> {
            final String recipient = privateMessageRecipientInputTextField.getText();
            final String message = messageInputTextField.getText();
            chatSession.sendPrivateMessage(recipient, message);
            messageInputTextField.setText("");
        });

        return sendPrivateMessageButton;
    }

    private JButton createSetUserNameButton() {
        final JButton setUserNameButton = new JButton("Set User Name");

        setUserNameButton.setName(ChatSessionGuiComponents.BUTTON_SET_USER_NAME);

        setUserNameButton.addActionListener(actionListener -> {
            final String userName = userNameInputTextField.getText();
            chatSession.setUserName(userName);
        });

        return setUserNameButton;
    }

    private JButton createBroadcastMessageButton() {
        final JButton broadcastMessageButton = new JButton("Broadcast Message");

        broadcastMessageButton.setName(ChatSessionGuiComponents.BUTTON_BROADCAST_MESSAGE);
        broadcastMessageButton.addActionListener(actionListener ->
        {
            chatSession.broadcastMessage(messageInputTextField.getText());
            messageInputTextField.setText("");
        });

        return broadcastMessageButton;
    }

    private JButton createQuitButton() {
        final JButton quitButton = new JButton("Quit");

        quitButton.addActionListener(actionListener -> endSession());
        quitButton.setName(ChatSessionGuiComponents.BUTTON_QUIT);

        return quitButton;
    }

    private JScrollPane createChatTextWindowScrollPane(final JTextArea chatMessagesWindow) {
        final JScrollPane chatMessagesScrollPane = new JScrollPane(chatMessagesWindow);

        chatMessagesScrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        chatMessagesScrollPane.setName(ChatSessionGuiComponents.SCROLL_PANE_CHAT_MESSAGES);

        return chatMessagesScrollPane;
    }

    public void start() {
        setVisible(true);
    }

    private void onServerResponseReceived(final String response) {
        chatMessagesWindow.append(response + System.lineSeparator());
    }

    private void endSession() {
        chatSession.quit();
        chatSession.stop();
    }

    @Override
    public void windowOpened(WindowEvent e) {

    }

    @Override
    public void windowClosing(WindowEvent e) {
        endSession();
    }

    @Override
    public void windowClosed(WindowEvent e) {

    }

    @Override
    public void windowIconified(WindowEvent e) {

    }

    @Override
    public void windowDeiconified(WindowEvent e) {

    }

    @Override
    public void windowActivated(WindowEvent e) {

    }

    @Override
    public void windowDeactivated(WindowEvent e) {

    }
}