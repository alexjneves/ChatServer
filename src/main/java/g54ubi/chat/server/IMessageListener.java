package g54ubi.chat.server;

/**
 * Listens for incoming messages and triggers the provided call back when one is received.
 */
public interface IMessageListener {
    /**
     * Start listening for messages. Potentially blocking.
     *
     * @param messageReceivedListener The callback to trigger when a message is received
     */
    void listen(final IMessageReceivedListener messageReceivedListener);

    /**
     * Stop listening for messages.
     */
    void stop();
}
