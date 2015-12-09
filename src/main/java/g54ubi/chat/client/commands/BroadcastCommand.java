package g54ubi.chat.client.commands;

import g54ubi.chat.common.CommandConstants;

public class BroadcastCommand implements IChatServerCommand {
    private final String message;

    public BroadcastCommand(final String message) {
        this.message = message;
    }

    @Override
    public String formMessage() {
        final String validMessage = message.trim();

        return CommandConstants.HAIL + " " + validMessage;
    }

    public String getMessage() {
        return message;
    }
}

