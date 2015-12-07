package g54ubi.chat.server;

/**
 * Listens for incoming resource T and triggers the provided call back when one is received.
 *
 * @param <T> The type of the resource to listen for
 */
public interface IResourceListener<T> {
    /**
     * Start listening for resources. Potentially blocking.
     *
     * @param resourceReceivedListener The callback to trigger when a resource is received
     */
    void listen(final IResourceReceivedListener<T> resourceReceivedListener);

    /**
     * Stop listening for resources.
     */
    void stop();
}
