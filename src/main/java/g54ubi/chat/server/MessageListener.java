package g54ubi.chat.server;

import java.io.IOException;

public final class MessageListener implements IMessageListener {
    private IChatClient chatClient;
    private IMessageReceivedListener messageReceivedListener;
    private volatile boolean running;

    public MessageListener(final IChatClient chatClient) {
        this.chatClient = chatClient;
        this.messageReceivedListener = null;
        this.running = false;
    }

    public void listen() {
        running = true;

        while (running) {
            try {
                final String message = chatClient.readMessage();
                if (messageReceivedListener != null) {
                    messageReceivedListener.onMessageReceived(message);
                }
            } catch (IOException e) {
                System.out.println("Read failed");
                System.exit(-1);
            }
        }
    }

    public void stop() {
        running = false;
    }

    public void registerMessageReceivedListener(final IMessageReceivedListener messageReceivedListener) {
        this.messageReceivedListener = messageReceivedListener;
    }
}
