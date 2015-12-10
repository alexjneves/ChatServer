package g54ubi.chat.client;

import g54ubi.chat.client.commands.ChatServerCommandFactory;
import g54ubi.chat.client.commands.IChatServerCommandFactory;
import g54ubi.chat.common.*;

import java.io.IOException;
import java.net.Socket;

public final class ChatSessionFactory implements IChatSessionFactory {
    private final String serverAddress;
    private final int serverPort;

    public ChatSessionFactory(final String serverAddress, final int serverPort) {
        this.serverAddress = serverAddress;
        this.serverPort = serverPort;
    }

    @Override
    public IChatSession create(final IResourceReceivedListener<String> responseReceivedListener) {
        IChatClient chatServer = null;

        try {
            final Socket serverSocket = new Socket(serverAddress, serverPort);
            chatServer = new ChatClient(serverSocket);
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(-1);
        }

        final IChatServerCommandFactory chatServerCommandFactory = new ChatServerCommandFactory();
        final IResourceListener<String> serverResponseListener = new ThreadedResourceListener<>(new ChatClientMessageListener(chatServer));

        final IChatSession chatSession = new ChatSession(chatServer, chatServerCommandFactory, serverResponseListener);
        chatSession.registerResponseListener(responseReceivedListener);

        return chatSession;
    }
}
