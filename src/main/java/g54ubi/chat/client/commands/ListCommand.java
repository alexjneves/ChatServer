package g54ubi.chat.client.commands;

import g54ubi.chat.common.CommandConstants;

public class ListCommand implements IChatServerCommand {
    @Override
    public String formMessage() {
        return CommandConstants.LIST;
    }
}
