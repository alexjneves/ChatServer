package g54ubi.chat.client;

import g54ubi.chat.client.commands.IChatServerCommand;
import g54ubi.chat.client.commands.IChatServerCommandFactory;
import g54ubi.chat.common.IChatClient;
import g54ubi.chat.common.IResourceListener;
import g54ubi.chat.common.IResourceReceivedListener;

public final class ChatSession implements IChatSession {
    private final IChatClient chatServer;
    private final IChatServerCommandFactory commandFactory;
    private final IResourceListener<String> serverResponseListener;

    private boolean running;
    private IResourceReceivedListener<String> registeredResponseReceivedListener;

    public ChatSession(final IChatClient chatServer, final IChatServerCommandFactory commandFactory, IResourceListener<String> serverResponseListener) {
        this.chatServer = chatServer;
        this.commandFactory = commandFactory;
        this.serverResponseListener = serverResponseListener;
        this.running = false;
    }

    @Override
    public void start() {
        running = true;
        serverResponseListener.listen(this::onServerResponseReceived);
    }

    @Override
    public void stop() {
        running = false;
        serverResponseListener.stop();
    }

    @Override
    public void listCurrentUsers() {
        final IChatServerCommand listCommand = commandFactory.createListCommand();
        chatServer.sendMessage(listCommand.formMessage());
    }

    @Override
    public void getSessionStatistics() {
        final IChatServerCommand statisticsCommand = commandFactory.createStatisticsCommand();
        chatServer.sendMessage(statisticsCommand.formMessage());
    }

    @Override
    public void quit() {
        final IChatServerCommand quitCommand = commandFactory.createQuitCommand();
        chatServer.sendMessage(quitCommand.formMessage());
    }

    @Override
    public void setUserName(final String userName) {
        final IChatServerCommand identityCommand = commandFactory.createIdentityCommand(userName);
        chatServer.sendMessage(identityCommand.formMessage());
    }

    @Override
    public void broadcastMessage(final String message) {
        final IChatServerCommand broadcastCommand = commandFactory.createBroadcastCommand(message);
        chatServer.sendMessage(broadcastCommand.formMessage());
    }

    @Override
    public void sendPrivateMessage(final String recipient, final String message) {
        final IChatServerCommand privateMessageCommand = commandFactory.createPrivateMessageCommand(recipient, message);
        chatServer.sendMessage(privateMessageCommand.formMessage());
    }

    @Override
    public void registerResponseListener(final IResourceReceivedListener<String> responseReceivedListener) {
        this.registeredResponseReceivedListener = responseReceivedListener;
    }

    private void onServerResponseReceived(final String serverResponse) {
        if (running && registeredResponseReceivedListener != null && serverResponse != null) {
            registeredResponseReceivedListener.onResourceReceived(serverResponse);
        }
    }
}
