package g54ubi.chat.client;

import g54ubi.chat.client.commands.IChatServerCommand;
import g54ubi.chat.common.IChatClient;
import g54ubi.chat.common.IResourceListener;
import g54ubi.chat.common.IResourceReceivedListener;

public final class ChatSession implements IChatSession {
    private final IChatClient chatServer;
    private final IChatServerCommandFactory commandFactory;
    private final IResourceListener<String> serverResponseListener;

    private IResourceReceivedListener<String> registeredResponseReceivedListener;

    public ChatSession(final IChatClient chatServer, final IChatServerCommandFactory commandFactory, IResourceListener<String> serverResponseListener) {
        this.chatServer = chatServer;
        this.commandFactory = commandFactory;
        this.serverResponseListener = serverResponseListener;
    }

    @Override
    public void start() {
        serverResponseListener.listen(this::onServerResponseReceived);
    }

    @Override
    public void stop() {
        serverResponseListener.stop();
    }

    @Override
    public void listCurrentUsers() {
        final IChatServerCommand listCommand = commandFactory.createListCommand();
        chatServer.sendMessage(listCommand.asServerMessage());
    }

    @Override
    public void getSessionStatistics() {
        final IChatServerCommand statisticsCommand = commandFactory.createStatisticsCommand();
        chatServer.sendMessage(statisticsCommand.asServerMessage());
    }

    @Override
    public void quit() {
        final IChatServerCommand quitCommand = commandFactory.createQuitCommand();
        chatServer.sendMessage(quitCommand.asServerMessage());
    }

    @Override
    public void setUserName(final String userName) {
        final IChatServerCommand identityCommand = commandFactory.createIdentityCommand(userName);
        chatServer.sendMessage(identityCommand.asServerMessage());
    }

    @Override
    public void broadcastMessage(final String message) {
        final IChatServerCommand broadcastCommand = commandFactory.createBroadcastCommand(message);
        chatServer.sendMessage(broadcastCommand.asServerMessage());
    }

    @Override
    public void sendPrivateMessage(final String recipient, final String message) {
        final IChatServerCommand privateMessageCommand = commandFactory.createPrivateMessageCommand(recipient, message);
        chatServer.sendMessage(privateMessageCommand.asServerMessage());
    }

    @Override
    public void registerResponseListener(final IResourceReceivedListener<String> responseReceivedListener) {
        this.registeredResponseReceivedListener = responseReceivedListener;
    }

    private void onServerResponseReceived(final String serverResponse) {
        if (registeredResponseReceivedListener != null) {
            registeredResponseReceivedListener.onResourceReceived(serverResponse);
        }
    }
}
