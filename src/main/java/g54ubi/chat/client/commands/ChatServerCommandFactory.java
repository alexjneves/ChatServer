package g54ubi.chat.client.commands;

public final class ChatServerCommandFactory implements IChatServerCommandFactory {
    @Override
    public IChatServerCommand createListCommand() {
        return new ListCommand();
    }

    @Override
    public IChatServerCommand createStatisticsCommand() {
        return new StatisticsCommand();
    }

    @Override
    public IChatServerCommand createQuitCommand() {
        return new QuitCommand();
    }

    @Override
    public IChatServerCommand createIdentityCommand(final String userName) {
        return new IdentityCommand(userName);
    }

    @Override
    public IChatServerCommand createBroadcastCommand(final String message) {
        return new BroadcastCommand(message);
    }

    @Override
    public IChatServerCommand createPrivateMessageCommand(final String recipient, final String message) {
        return new PrivateMessageCommand(recipient, message);
    }
}
