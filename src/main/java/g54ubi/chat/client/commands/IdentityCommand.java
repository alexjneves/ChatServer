package g54ubi.chat.client.commands;

import g54ubi.chat.common.CommandConstants;

public class IdentityCommand implements IChatServerCommand {
    private final String userName;

    public IdentityCommand(final String userName) {
        this.userName = userName;
    }

    @Override
    public String formMessage() {
        final String validUserName = userName.replaceAll("\\s", "");

        return CommandConstants.IDEN + " " + validUserName;
    }

    public String getUserName() {
        return userName;
    }
}
