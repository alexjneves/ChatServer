package g54ubi.chat.server;

import java.io.IOException;

public final class ChatClientMessageListener implements IResourceListener<String> {
    private IChatClient chatClient;
    private volatile boolean running;

    public ChatClientMessageListener(final IChatClient chatClient) {
        this.chatClient = chatClient;
        this.running = false;
    }

    @Override
    public void listen(final IResourceReceivedListener<String> messageReceivedListener) {
        running = true;

        while (running) {
            try {
                final String message = chatClient.readMessage();
                messageReceivedListener.onResourceReceived(message);
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

