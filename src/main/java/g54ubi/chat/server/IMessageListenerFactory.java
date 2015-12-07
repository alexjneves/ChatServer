package g54ubi.chat.server;

/**
 * A factory that creates IMessageListeners
 */
public interface IMessageListenerFactory {
    /**
     * Create a new Message Listener which will trigger the supplied callback when a message is received.
     *
     * @param messageReceivedListener The callback to trigger when a message is received
     * @return The Message Listener instance
     */
    IMessageListener create(final IMessageReceivedListener messageReceivedListener);
}
