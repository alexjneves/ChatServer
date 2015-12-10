package g54ubi.chat.client;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

public final class ChatSessionGui extends JFrame implements WindowListener {
    private static final int INITIAL_FRAME_WIDTH = 800;
    private static final int INITIAL_FRAME_HEIGHT = 800;

    private final IChatSession chatSession;
    private final JTextArea chatTextWindow;
    private final JTextField messageInputTextField;
    private final JTextField userNameInputTextField;
    private final JTextField privateMessageRecipientInputTextField;

    public ChatSessionGui(final IChatSession chatSession) {
        super();
        this.chatSession = chatSession;
        this.chatSession.registerResponseListener(this::onServerResponseReceived);

        addWindowListener(this);

        this.chatTextWindow = createChatTextWindow();
        this.messageInputTextField = createMessageInputTextField();
        this.userNameInputTextField = createUserNameInputTextField();
        this.privateMessageRecipientInputTextField = createPrivateMessageRecipientInputTextField();

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
        panel.add(createChatTextWindowScrollPane(this.chatTextWindow), c);

        // ROW 6
        panel.add(messageInputTextField, createConstraints(5, 0, 3));
    }

    private JTextArea createChatTextWindow() {
        final JTextArea chatTextWindow = new JTextArea();
        chatTextWindow.setLineWrap(true);
        return chatTextWindow;
    }

    private JTextField createMessageInputTextField() {
        final JTextField messageInputTextField = new JTextField();
        messageInputTextField.setMaximumSize(new Dimension(Integer.MAX_VALUE, messageInputTextField.getPreferredSize().height));
        return messageInputTextField;
    }

    private JTextField createUserNameInputTextField() {
        final JTextField userNameInputTextField = new JTextField(10);
        userNameInputTextField.setMaximumSize(new Dimension(userNameInputTextField.getPreferredSize().width, userNameInputTextField.getPreferredSize().height));
        return userNameInputTextField;
    }

    private JTextField createPrivateMessageRecipientInputTextField() {
        final JTextField privateMessageRecipientInputTextField = new JTextField();
        privateMessageRecipientInputTextField.setMaximumSize(new Dimension(Integer.MAX_VALUE, privateMessageRecipientInputTextField.getPreferredSize().height));
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
        startSessionButton.addActionListener(actionListener -> chatSession.start());
        return startSessionButton;
    }

    private JLabel createUserNameInputTextFieldLabel() {
        final JLabel userNameInputTextFieldLabel = new JLabel("User Name:");
        userNameInputTextFieldLabel.setLabelFor(userNameInputTextField);
        return userNameInputTextFieldLabel;
    }

    private JButton createListCurrentUsersButton() {
        final JButton listCurrentUsersButton = new JButton("List Current Users");
        listCurrentUsersButton.addActionListener(actionListener -> chatSession.listCurrentUsers());
        return listCurrentUsersButton;
    }

    private JButton createShowSessionStatisticsButton() {
        final JButton showSessionStatisticsButton = new JButton("Show Session Statistics");
        showSessionStatisticsButton.addActionListener(actionListener -> chatSession.getSessionStatistics());
        return showSessionStatisticsButton;
    }

    private JLabel createPrivateMessageRecipientInputTextFieldLabel() {
        final JLabel privateMessageRecipientInputTextFieldLabel = new JLabel("Private Message Recipient:");
        privateMessageRecipientInputTextFieldLabel.setLabelFor(privateMessageRecipientInputTextField);
        return privateMessageRecipientInputTextFieldLabel;
    }

    private JButton createSendPrivateMessageButton() {
        final JButton sendPrivateMessageButton = new JButton("Send Private Message");
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
        setUserNameButton.addActionListener(actionListener -> {
            final String userName = userNameInputTextField.getText();
            chatSession.setUserName(userName);
            userNameInputTextField.setEditable(false);
        });
        return setUserNameButton;
    }

    private JButton createBroadcastMessageButton() {
        final JButton broadcastMessageButton = new JButton("Broadcast Message");
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
        return quitButton;
    }

    private JScrollPane createChatTextWindowScrollPane(final JTextArea chatTextWindow) {
        final JScrollPane scrollPane = new JScrollPane(chatTextWindow);
        scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        return scrollPane;
    }

    public void start() {
        setVisible(true);
    }

    private void onServerResponseReceived(final String response) {
        chatTextWindow.append(response + System.lineSeparator());
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