package g54ubi.chat.server;

import java.io.IOException;

/**
 * Represents a client that is connected to the ChatServer and handles communication with them.
 */
public interface IChatClient {
    /**
     * Closes the clients connection to the ChatServer.
     * @throws IOException
     */
    void close() throws IOException;

    /**
     * Reads a message from the client. Potentially blocking.
     *
     * @return The read message
     * @throws IOException
     */
    String readMessage() throws IOException;

    /**
     * Sends a message to the client.
     *
     * @param message The message to send
     */
    void sendMessage(String message);
}
