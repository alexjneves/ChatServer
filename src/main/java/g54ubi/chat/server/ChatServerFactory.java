package g54ubi.chat.server;

import java.io.IOException;

public final class ChatServerFactory {
    private final int port;

    public ChatServerFactory(final int port) {
        this.port = port;
    }

    public ChatServer create() {
        IChatServerSocket chatServerSocket = null;

        try {
            chatServerSocket = new ChatServerSocket(port);
        } catch (IOException e) {
            System.err.println("Error Initialising ChatServerSocket");
            e.printStackTrace();
            System.exit(-1);
        }

        System.out.println("ChatServer has been initialised on port " + port);

        final IConnectionListenerFactory connectionListenerFactory = new ConnectionListenerFactory(chatServerSocket);

        return new ChatServer(connectionListenerFactory);
    }
}
