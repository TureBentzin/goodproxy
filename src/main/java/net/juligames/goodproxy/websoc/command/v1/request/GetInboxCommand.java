package net.juligames.goodproxy.websoc.command.v1.request;

import net.juligames.goodproxy.util.Credentials;
import net.juligames.goodproxy.websoc.action.Action;
import org.jetbrains.annotations.NotNull;

public class GetInboxCommand extends AuthenticatedCommand {

    private int messageID = -1;

    public GetInboxCommand(@NotNull Credentials credentials) {
        super(credentials);
    }

    public GetInboxCommand(@NotNull Credentials credentials, int messageID) {
        super(credentials);
        this.messageID = messageID;
    }

    @Override
    @NotNull Action getAction() {
        return Action.GET_INBOX;
    }

    @Override
    protected @NotNull String getValue3() {
        return String.valueOf(messageID);
    }
}
