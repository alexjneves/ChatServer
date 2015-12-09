package g54ubi.chat.client;

import g54ubi.chat.server.IResourceReceivedListener;

public interface IChatSession {
    void start();
    void stop();
    void listCurrentUsers();
    void getSessionStatistics();
    void quit();
    void setUserName(final String userName);
    void broadcastMessage(final String message);
    void sendPrivateMessage(final String recipient, final String message);
    void registerResponseListener(final IResourceReceivedListener<String> responseReceivedListener);
}
