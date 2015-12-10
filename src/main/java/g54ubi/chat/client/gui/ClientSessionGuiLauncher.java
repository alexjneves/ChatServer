package g54ubi.chat.client.gui;

import g54ubi.chat.client.ChatSessionFactory;
import g54ubi.chat.client.IChatSessionFactory;

public final class ClientSessionGuiLauncher {
    private static final String SERVER_ADDRESS = "localhost";
    private static final int SERVER_PORT = 9000;

    public static void main(String[] args) {
        final IChatSessionFactory chatSessionFactory = new ChatSessionFactory(SERVER_ADDRESS, SERVER_PORT);

        final ChatSessionGui chatSessionGui = new ChatSessionGui(chatSessionFactory);
        chatSessionGui.start();
    }
}
