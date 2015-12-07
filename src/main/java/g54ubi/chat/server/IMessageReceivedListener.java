package g54ubi.chat.server;

/**
 * An interface for a message received callback.
 */
public interface IMessageReceivedListener {
    /**
     * The callback to be triggered when a message is received.
     *
     * @param message The received message
     */
    void onMessageReceived(final String message);
}
