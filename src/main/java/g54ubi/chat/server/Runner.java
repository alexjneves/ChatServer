package g54ubi.chat.server;

public final class Runner {
    private final static int PORT = 9000;

    public static void main(String[] args){
        final ChatServerFactory chatServerFactory = new ChatServerFactory(PORT);
        final ChatServer chatServer = chatServerFactory.create();
        chatServer.start();
	}
}
