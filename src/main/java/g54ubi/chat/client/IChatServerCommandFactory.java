package g54ubi.chat.client;

public interface IChatServerCommandFactory {
    String createListCommand();
    String createStatisticsCommand();
    String createQuitCommand();
    String createIdentityCommand(final String userName);
    String createBroadcastCommand(final String message);
    String createPrivateMessageCommand(final String recipient, final String message);
}
