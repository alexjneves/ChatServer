package g54ubi.chat.server;

import java.io.IOException;

public final class ChatClientMessageListener implements IMessageListener {
    private IChatClient chatClient;
    private volatile boolean running;

    public ChatClientMessageListener(final IChatClient chatClient) {
        this.chatClient = chatClient;
        this.running = false;
    }

    @Override
    public void listen(final IMessageReceivedListener messageReceivedListener) {
        running = true;

        while (running) {
            try {
                final String message = chatClient.readMessage();
                messageReceivedListener.onMessageReceived(message);
            } catch (IOException e) {
                System.out.println("Read failed");
                System.exit(-1);
            }
        }
    }

    @Override
    public void stop() {
        running = false;
    }
}

