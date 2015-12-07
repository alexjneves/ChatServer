package g54ubi.chat.server;

import java.io.IOException;

public final class ChatClientMessageListener implements IMessageListener {
    private IChatClient chatClient;
    private IMessageReceivedListener messageReceivedListener;
    private volatile boolean running;

    public ChatClientMessageListener(final IChatClient chatClient, final IMessageReceivedListener messageReceivedListener) {
        this.chatClient = chatClient;
        this.messageReceivedListener = messageReceivedListener;
        this.running = false;
    }

    @Override
    public void listen() {
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

