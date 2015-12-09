package g54ubi.chat.client;

import g54ubi.chat.common.IResourceListener;
import g54ubi.chat.common.IResourceReceivedListener;

/**
 * A wrapper around a resource listener which will spawn a new thread to listen on.
 * @param <T> The type of the resource to listen for
 */
public final class ThreadedResourceListener<T> implements IResourceListener<T> {
    private final IResourceListener<T> resourceListener;

    public ThreadedResourceListener(final IResourceListener<T> resourceListener) {
        this.resourceListener = resourceListener;
    }

    @Override
    public void listen(final IResourceReceivedListener<T> resourceReceivedListener) {
        new Thread(() -> {
            resourceListener.listen(resourceReceivedListener);
        }).start();
    }

    @Override
    public void stop() {
        resourceListener.stop();
    }
}
