package g54ubi.chat.client;

import g54ubi.chat.server.IResourceListener;
import g54ubi.chat.server.IResourceReceivedListener;

public final class ThreadedResourceListener<T> implements IResourceListener<T> {
    private final IResourceListener<T> resourceListener;

    public ThreadedResourceListener(final IResourceListener<T> resourceListener) {
        this.resourceListener = resourceListener;
    }

    @Override
    public void listen(IResourceReceivedListener<T> resourceReceivedListener) {
        new Thread(() -> {
            resourceListener.listen(resourceReceivedListener);
        }).start();
    }

    @Override
    public void stop() {
        resourceListener.stop();
    }
}
