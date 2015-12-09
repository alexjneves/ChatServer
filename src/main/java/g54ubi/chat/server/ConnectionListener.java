package g54ubi.chat.server;

import g54ubi.chat.common.ChatClientMessageListener;
import g54ubi.chat.common.IChatClient;
import g54ubi.chat.common.IResourceListener;
import g54ubi.chat.common.IResourceReceivedListener;

import java.io.IOException;

public final class ConnectionListener implements IResourceListener<IConnection> {
    private final IChatServerSocket chatServerSocket;
    private final IChatServer chatServer;
    private volatile boolean running;

    public ConnectionListener(final IChatServerSocket chatServerSocket, final IChatServer chatServer) {
        this.chatServerSocket = chatServerSocket;
        this.chatServer = chatServer;
        this.running = false;
    }

    @Override
    public void listen(IResourceReceivedListener<IConnection> connectionReceivedListener) {
        running = true;

        while (running) {
            Connection connection = null;
            try {
                final IChatClient chatClient = chatServerSocket.accept();
                final IResourceListener<String> messageListener = new ChatClientMessageListener(chatClient);

                connection = new Connection(chatClient, chatServer, messageListener);
            }
            catch (IOException e) {
                System.err.println("error setting up new client conneciton");
                e.printStackTrace();
            }

            Thread t = new Thread(connection);
            t.start();

            connectionReceivedListener.onResourceReceived(connection);
        }
    }

    @Override
    public void stop() {
        running = false;

        try {
            chatServerSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
