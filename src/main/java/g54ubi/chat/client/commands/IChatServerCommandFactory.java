package g54ubi.chat.client.commands;

import g54ubi.chat.client.commands.IChatServerCommand;

/**
 * A factory for creating commands that can be handled by the chat server.
 */
public interface IChatServerCommandFactory {
    /**
     * Creates a list command.
     *
     * @return A list chat server command
     */
    IChatServerCommand createListCommand();

    /**
     * Creates statistics command.
     *
     * @return A statistics chat server command
     */
    IChatServerCommand createStatisticsCommand();

    /**
     * Creates a quit command.
     *
     * @return A quit chat server command
     */
    IChatServerCommand createQuitCommand();

    /**
     * Creates an identity command with the specified user name.
     *
     * @param userName The user name to include with the command
     * @return A identity chat server command
     */
    IChatServerCommand createIdentityCommand(final String userName);

    /**
     * Creates a broadcast command with the message to be sent.
     *
     * @param message The message to broadcast
     * @return A broadcast chat server command
     */
    IChatServerCommand createBroadcastCommand(final String message);

    /**
     * Creates a private message command with the specified recipient and the message to be sent.
     *
     * @param recipient The recipient of the message
     * @param message The message to be sent
     * @return A private message chat server command
     */
    IChatServerCommand createPrivateMessageCommand(final String recipient, final String message);
}
