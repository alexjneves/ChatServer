package g54ubi.chat.client;

/**
 * A factory for creating commands that can be handled by the chat server.
 */
public interface IChatServerCommandFactory {
    /**
     * Creates a valid LIST command.
     *
     * @return LIST command
     */
    String createListCommand();

    /**
     * Creates a valid STAT command.
     *
     * @return STAT command
     */
    String createStatisticsCommand();

    /**
     * Creates a valid QUIT command.
     *
     * @return QUIT command
     */
    String createQuitCommand();

    /**
     * Creates a valid IDEN command with the specified user name.
     *
     * @param userName The user name to include with the command
     * @return IDEN command
     */
    String createIdentityCommand(final String userName);

    /**
     * Creates a valid HAIL command with the message to be sent.
     *
     * @param message The message to broadcast
     * @return HAIL command
     */
    String createBroadcastCommand(final String message);

    /**
     * Creates a valid MESG command with the specified recipient and the message to be sent.
     *
     * @param recipient The recipient of the message
     * @param message The message to be sent
     * @return MESG command
     */
    String createPrivateMessageCommand(final String recipient, final String message);
}
