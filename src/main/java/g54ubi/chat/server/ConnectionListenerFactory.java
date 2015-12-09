package g54ubi.chat.server;

import g54ubi.chat.common.IResourceListener;

public final class ConnectionListenerFactory implements IConnectionListenerFactory {
    private final IChatServerSocket chatServerSocket;

    public ConnectionListenerFactory(final IChatServerSocket chatServerSocket) {
        this.chatServerSocket = chatServerSocket;
    }

    @Override
    public IResourceListener<IConnection> create(final IChatServer chatServer) {
        return new ConnectionListener(chatServerSocket, chatServer);
    }
}
