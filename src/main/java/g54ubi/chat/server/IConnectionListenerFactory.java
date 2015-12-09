package g54ubi.chat.server;

import g54ubi.chat.common.IResourceListener;

/**
 * A factory for creating connection listeners
 */
public interface IConnectionListenerFactory {
    /**
     * Creates a new instance of a connection listener.
     *
     * @param chatServer The chat sever the connection will interact with
     * @return The connection listener
     */
    IResourceListener<IConnection> create(final IChatServer chatServer);
}

