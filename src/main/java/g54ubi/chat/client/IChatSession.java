package g54ubi.chat.client;

import g54ubi.chat.common.IResourceReceivedListener;

/**
 * Represents a chat session with the server. Provides various asynchronous interactions. Server responses are
 * returned via a registered callback.
 */
public interface IChatSession {
    /**
     * Start listening to server responses.
     */
    void start();

    /**
     * Stop listening to server responses.
     */
    void stop();

    /**
     * List all users currently connected to the server.
     */
    void listCurrentUsers();

    /**
     * Get statistics from the current chat session.
     */
    void getSessionStatistics();

    /**
     * Terminate the connection to the server, ending the session.
     */
    void quit();

    /**
     *  Sets the user name of the client.
     *
     * @param userName The user name to register with the server
     */
    void setUserName(final String userName);

    /**
     * Broadcast a message to all connected users.
     *
     * @param message The message to broadcast
     */
    void broadcastMessage(final String message);

    /**
     * Send a message only to the specified user.
     *
     * @param recipient The user to receive the message
     * @param message The message to send
     */
    void sendPrivateMessage(final String recipient, final String message);

    /**
     * Register a callback to be triggered when a message has been received from the server.
     *
     * @param responseReceivedListener The callback to be triggered
     */
    void registerResponseListener(final IResourceReceivedListener<String> responseReceivedListener);
}
