package g54ubi.chat.client.commands;

import g54ubi.chat.client.commands.IChatServerCommand;
import g54ubi.chat.client.commands.IChatServerCommandFactory;

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
    public IChatServerCommand createIdentityCommand(String userName) {
        return null;
    }

    @Override
    public IChatServerCommand createBroadcastCommand(String message) {
        return null;
    }

    @Override
    public IChatServerCommand createPrivateMessageCommand(String recipient, String message) {
        return null;
    }
}