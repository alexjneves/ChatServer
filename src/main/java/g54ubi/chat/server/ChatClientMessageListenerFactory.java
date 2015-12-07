package g54ubi.chat.server;

public final class ChatClientMessageListenerFactory implements IMessageListenerFactory {
    private final IChatClient chatClient;

    public ChatClientMessageListenerFactory(final IChatClient chatClient) {
        this.chatClient = chatClient;
    }

    @Override
    public IMessageListener create(IMessageReceivedListener messageReceivedListener) {
        return new ChatClientMessageListener(chatClient, messageReceivedListener);
    }
}
