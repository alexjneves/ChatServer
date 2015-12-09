package g54ubi.chat.common;

import java.io.IOException;

/**
 * Represents a connection to a chat client and handles communication with them.
 */
public interface IChatClient {
    /**
     * Closes the connection to the client.
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
