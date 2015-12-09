package g54ubi.chat.server;

import g54ubi.chat.common.IChatClient;

import java.io.IOException;

/**
 * A socket which listens for incoming chat server clients.
 */
public interface IChatServerSocket {
    /**
     * Accept an incoming client. Potentially blocking.
     *
     * @return The chat client
     * @throws IOException
     */
    IChatClient accept() throws IOException;

    /**
     * Closes the socket, stopping incoming connections.
     *
     * @throws IOException
     */
    void close() throws IOException;
}
