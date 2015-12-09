package g54ubi.chat.client;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

public final class ChatSessionGui extends JFrame implements WindowListener {
    private final IChatSession chatSession;
    private final JTextField inputTextField;
    private final JTextArea textWindow;

    public ChatSessionGui(final IChatSession chatSession) {
        super();
        this.chatSession = chatSession;
        this.chatSession.registerResponseListener(this::onServerResponseReceived);

        addWindowListener(this);

        inputTextField = new JTextField();
        inputTextField.setMaximumSize(new Dimension(Integer.MAX_VALUE, inputTextField.getPreferredSize().height) );

        textWindow = new JTextArea();

        initialise();
    }

    private void initialise() {
        setSize(800, 800);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        final JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        add(panel);

        createStartSessionButton(panel);
        createSetUserNameButton(panel);
        createBroadcastMessageButton(panel);
        createQuitButton(panel);

        createTextWindow(panel);

        panel.add(inputTextField);
    }

    private void createStartSessionButton(final JPanel panel) {
        final JButton startSessionButton = new JButton("Start Session");
        startSessionButton.addActionListener(actionListener -> chatSession.start());
        panel.add(startSessionButton);
    }

    private void createSetUserNameButton(final JPanel panel) {
        final JButton setUserNameButton = new JButton("Set User Name");
        setUserNameButton.addActionListener(actionListener -> chatSession.setUserName("Alex"));
        panel.add(setUserNameButton);
    }

    private void createBroadcastMessageButton(JPanel panel) {
        final JButton broadcastMessageButton = new JButton("Broadcast Message");
        broadcastMessageButton.addActionListener(actionListener ->
        {
            chatSession.broadcastMessage(inputTextField.getText());
            inputTextField.setText("");
        });
        panel.add(broadcastMessageButton);
    }

    private void createQuitButton(JPanel panel) {
        final JButton quitButton = new JButton("Quit");
        quitButton.addActionListener(actionListener -> exit());
        panel.add(quitButton);
    }

    private void createTextWindow(JPanel panel) {
        final JScrollPane scrollPane = new JScrollPane(textWindow);
        scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        panel.add(scrollPane);
    }

    public void start() {
        setVisible(true);
    }

    private void onServerResponseReceived(final String response) {
        textWindow.append(response + System.lineSeparator());
    }

    private void exit() {
        chatSession.quit();
        chatSession.stop();
    }

    @Override
    public void windowOpened(WindowEvent e) {

    }

    @Override
    public void windowClosing(WindowEvent e) {
        exit();
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