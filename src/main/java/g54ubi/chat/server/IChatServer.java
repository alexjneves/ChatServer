package g54ubi.chat.server;

import java.util.ArrayList;

/**
 * A chat server which accepts incoming clients and handles communication between them.
 */
public interface IChatServer {
    /**
     * Start the Chat Server, accepting incoming clients.
     */
    void start();

    /**
     * Returns all currently connected clients.
     *
     * @return The collection of client user names.
     */
    ArrayList<String> getUserList();

    /**
     * Determines whether the given user is currently connected to the Chat Server.
     *
     * @param newUser The user name to query
     * @return True is the user is connected
     */
    boolean doesUserExist(String newUser);

    /**
     * Broadcasts the given message to all connected clients.
     *
     * @param theMessage The message to broadcast
     */
    void broadcastMessage(String theMessage);

    /**
     * Send the given message to a particular user if that user is currently connected.
     *
     * @param message The message to send
     * @param user The recipient
     *
     * @return True if the message was sent
     */
    boolean sendPrivateMessage(String message, String user);

    /**
     * Clean up all disconnected clients.
     */
    void removeDeadUsers();

    /**
     * Returns the number of currently connected clients.
     *
     * @return The number of connected clients
     */
    int getNumberOfUsers();
}
