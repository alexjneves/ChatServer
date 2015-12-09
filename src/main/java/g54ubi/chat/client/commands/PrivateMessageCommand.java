package g54ubi.chat.client.commands;

import g54ubi.chat.common.CommandConstants;

public class PrivateMessageCommand implements IChatServerCommand {
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

        return CommandConstants.MESG + " " + validRecipient + " " + validMessage;
    }

    public String getRecipient() {
        return recipient;
    }

    public String getMessage() {
        return message;
    }
}
