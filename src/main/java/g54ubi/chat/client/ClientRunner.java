package g54ubi.chat.client;

import g54ubi.chat.client.commands.ChatServerCommandFactory;
import g54ubi.chat.client.commands.IChatServerCommandFactory;
import g54ubi.chat.common.ChatClient;
import g54ubi.chat.common.ChatClientMessageListener;
import g54ubi.chat.common.IChatClient;
import g54ubi.chat.common.IResourceListener;

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

        final IChatSession chatSession = new ChatSession(chatServer, chatServerCommandFactory, serverResponseListener);
        chatSession.registerResponseListener(ClientRunner::listen);
        chatSession.start();

        chatSession.setUserName("Alex");
        chatSession.broadcastMessage("Hi everyone");
        chatSession.listCurrentUsers();
        chatSession.getSessionStatistics();
        chatSession.quit();

        chatSession.stop();
    }

    public static void listen(final String serverMessage) {
        System.out.println("Server: " + serverMessage);
    }
}
