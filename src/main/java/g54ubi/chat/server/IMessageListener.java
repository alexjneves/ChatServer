package g54ubi.chat.server;

/**
 * Listens for incoming messages and triggers the provided call back when one is received.
 */
public interface IMessageListener {
    void listen();
    void stop();
}
