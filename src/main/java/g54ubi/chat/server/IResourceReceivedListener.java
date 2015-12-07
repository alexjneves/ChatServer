package g54ubi.chat.server;

/**
 * An interface for a resource received callback.
 *
 * @param <T> The type of the resource to return
 */
public interface IResourceReceivedListener<T> {
    /**
     * The callback to be triggered when a resource is received.
     *
     * @param resource The received resource
     */
    void onResourceReceived(final T resource);
}
