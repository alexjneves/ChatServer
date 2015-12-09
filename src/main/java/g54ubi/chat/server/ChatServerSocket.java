package g54ubi.chat.server;

import g54ubi.chat.common.ChatClient;
import g54ubi.chat.common.IChatClient;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * A lightweight wrapper for java.net.ServerSocket. Listens for incoming connections on the specified port.
 */
public final class ChatServerSocket implements IChatServerSocket {
    private final ServerSocket serverSocket;

    public ChatServerSocket(int port) throws IOException {
        serverSocket = new ServerSocket(port);
    }

    @Override
    public IChatClient accept() throws IOException {
        final Socket socket = serverSocket.accept();
        return new ChatClient(socket);
    }

    @Override
    public void close() throws IOException {
        serverSocket.close();
    }
}
