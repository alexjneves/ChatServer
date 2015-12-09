package g54ubi.chat.client;

public final class ChatServerCommandFactory implements IChatServerCommandFactory {
    @Override
    public String createListCommand() {
        return null;
    }

    @Override
    public String createStatisticsCommand() {
        return null;
    }

    @Override
    public String createQuitCommand() {
        return null;
    }

    @Override
    public String createIdentityCommand(String userName) {
        return null;
    }

    @Override
    public String createBroadcastCommand(String message) {
        return null;
    }

    @Override
    public String createPrivateMessageCommand(String recipient, String message) {
        return null;
    }
}
