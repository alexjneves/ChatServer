package g54ubi.chat.client.commands;

public class PrivateMessageCommand implements IChatServerCommand {
    private static final String PRIVATE_MESSAGE_COMMAND = "MESG";

    private final String recipient;
    private final String message;

    public PrivateMessageCommand(final String recipient, final String message) {
        this.recipient = recipient;
        this.message = message;
    }

    @Override
    public String formMessage() {
        final String validRecipient = recipient.replaceAll("\\s", "");
        final String validMessage = message.trim();

        return PRIVATE_MESSAGE_COMMAND + " " + validRecipient + " " + validMessage;
    }
}
