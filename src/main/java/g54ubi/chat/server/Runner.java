package g54ubi.chat.server;

import java.io.IOException;

public final class Runner {
    private final static int PORT = 9000;

    public static void main(String[] args){
        IChatServerSocket chatServerSocket = null;

        try {
            chatServerSocket = new ChatServerSocket(PORT);
        } catch (IOException e) {
            System.err.println("Error Initialising ChatServerSocket");
            e.printStackTrace();
            System.exit(-1);
        }

        System.out.println("ChatServer has been initialised on port " + PORT);

        final IChatServer server = new ChatServer(chatServerSocket);
        server.run();
	}
}
