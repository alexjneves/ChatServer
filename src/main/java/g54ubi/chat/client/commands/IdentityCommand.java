package g54ubi.chat.client.commands;

public class IdentityCommand implements IChatServerCommand {
    private static final String IDENTITY_COMMAND = "IDEN";

    private final String userName;

    public IdentityCommand(final String userName) {
        this.userName = userName;
    }

    @Override
    public String formMessage() {
        final String validUserName = userName.replaceAll("\\s", "");

        return IDENTITY_COMMAND + " " + validUserName;
    }
}
