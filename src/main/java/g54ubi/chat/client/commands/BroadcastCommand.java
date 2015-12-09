package g54ubi.chat.client.commands;

public class BroadcastCommand implements IChatServerCommand {
    private static final String BROADCAST_COMMAND = "HAIL";

    private final String message;

    public BroadcastCommand(final String message) {
        this.message = message;
    }

    @Override
    public String formMessage() {
        final String validMessage = message.trim();

        return BROADCAST_COMMAND + " " + validMessage;
    }
}

