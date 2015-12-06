package g54ubi.chat.server;

public interface IMessageListener {
    void listen();
    void stop();
    void registerMessageReceivedListener(final IMessageReceivedListener messageReceivedListener);
}
