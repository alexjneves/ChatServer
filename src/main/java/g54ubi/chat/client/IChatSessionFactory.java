package g54ubi.chat.client;

import g54ubi.chat.common.IResourceReceivedListener;

public interface IChatSessionFactory {
    IChatSession create(final IResourceReceivedListener<String> responseReceivedListener);
}
