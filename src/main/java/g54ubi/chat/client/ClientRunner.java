package g54ubi.chat.client;

import g54ubi.chat.server.ChatClient;
import g54ubi.chat.server.ChatClientMessageListener;
import g54ubi.chat.server.IChatClient;
import g54ubi.chat.server.IResourceListener;

import java.io.IOException;
import java.net.Socket;

public final class ClientRunner {
    private static final String SERVER_ADDRESS = "localhost";
    private static final int SERVER_PORT = 9000;

    public static void main(String[] args) {
        IChatClient chatServer = null;

        try {
            final Socket serverSocket = new Socket(SERVER_ADDRESS, SERVER_PORT);
            chatServer = new ChatClient(serverSocket);
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(-1);
        }

        final IChatServerCommandFactory chatServerCommandFactory = new ChatServerCommandFactory();
        final IResourceListener<String> serverResponseListener = new ThreadedResourceListener<>(new ChatClientMessageListener(chatServer));

        final IChatSession chatSession = new ChatSession(chatServer, chatServerCommandFactory, (serverResponseListener));
    }
}
