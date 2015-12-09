package g54ubi.chat.client.commands;

import g54ubi.chat.common.CommandConstants;

public class QuitCommand implements IChatServerCommand {
    @Override
    public String asServerMessage() {
        return CommandConstants.QUIT;
    }
}
