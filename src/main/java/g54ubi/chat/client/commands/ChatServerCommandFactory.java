package g54ubi.chat.client.commands;

public final class ChatServerCommandFactory implements IChatServerCommandFactory {
    @Override
    public IChatServerCommand createListCommand() {
        return null;
    }

    @Override
    public IChatServerCommand createStatisticsCommand() {
        return null;
    }

    @Override
    public IChatServerCommand createQuitCommand() {
        return null;
    }

    @Override
    public IChatServerCommand createIdentityCommand(final String userName) {
        return null;
    }

    @Override
    public IChatServerCommand createBroadcastCommand(final String message) {
        return null;
    }

    @Override
    public IChatServerCommand createPrivateMessageCommand(final String recipient, final String message) {
        return null;
    }
}
