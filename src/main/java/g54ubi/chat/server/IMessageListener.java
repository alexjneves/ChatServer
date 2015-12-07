package g54ubi.chat.server;

/**
 * Listens for incoming messages and triggers the provided call back when one is received.
 */
public interface IMessageListener {
    /**
     * Start listening for messages. Potentially blocking.
     */
    void listen();

    /**
     * Stop listening for messages.
     */
    void stop();
}
