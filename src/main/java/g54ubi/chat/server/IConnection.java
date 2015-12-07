package g54ubi.chat.server;

/**
 * A connection which manages the communication between a particular client and the server.
 */
public interface IConnection {
    /**
     * Indicates if the connection is active, i.e. is the client still connected
     *
     * @return True if the connection is active
     */
    boolean isRunning();

    /**
     * Sends the specified message to the client.
     *
     * @param message The message to be sent
     */
    void messageForConnection(String message);

    /**
     * Indicates the current state of the client in relation to the server.
     *
     * @return REGISTERED or UNREGISTERED
     */
    int getState();

    /**
     * Indicates the client's user name
     *
     * @return The user name of the client
     */
    String getUserName();
}
