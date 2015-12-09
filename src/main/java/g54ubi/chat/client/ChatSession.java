package g54ubi.chat.client;

import g54ubi.chat.common.IChatClient;
import g54ubi.chat.common.IResourceListener;
import g54ubi.chat.common.IResourceReceivedListener;

public final class ChatSession implements IChatSession {
    private final IChatClient chatServer;
    private final IChatServerCommandFactory commandFactory;
    private final IResourceListener<String> serverResponseListener;

    public ChatSession(final IChatClient chatServer, final IChatServerCommandFactory commandFactory, IResourceListener<String> serverResponseListener) {
        this.chatServer = chatServer;
        this.commandFactory = commandFactory;
        this.serverResponseListener = serverResponseListener;
    }

    @Override
    public void start() {

    }

    @Override
    public void stop() {

    }

    @Override
    public void listCurrentUsers() {

    }

    @Override
    public void getSessionStatistics() {

    }

    @Override
    public void quit() {

    }

    @Override
    public void setUserName(String userName) {

    }

    @Override
    public void broadcastMessage(String message) {

    }

    @Override
    public void sendPrivateMessage(String recipient, String message) {

    }

    @Override
    public void registerResponseListener(IResourceReceivedListener<String> responseReceivedListener) {

    }
}
