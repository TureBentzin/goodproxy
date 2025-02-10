package net.juligames.goodproxy.websoc.command.v1.request;

import net.juligames.goodproxy.util.Credentials;
import net.juligames.goodproxy.websoc.action.Action;
import org.jetbrains.annotations.NotNull;

public class GetInboxCommand extends AuthenticatedCommand {

    private int messageID = -1;
    private final boolean privateMessage;

    public GetInboxCommand(@NotNull Credentials credentials) {
        super(credentials);
        this.privateMessage = false;
    }

    public GetInboxCommand(@NotNull Credentials credentials, int messageID) {
        super(credentials);
        this.messageID = messageID;
        this.privateMessage = false;

    }

    public GetInboxCommand(@NotNull Credentials credentials, boolean privateMessage) {
        super(credentials);
        this.privateMessage = privateMessage;
    }

    public GetInboxCommand(@NotNull Credentials credentials, int messageID, boolean privateMessage) {
        super(credentials);
        this.messageID = messageID;
        this.privateMessage = privateMessage;

    }

    @Override
    @NotNull Action getAction() {
        return privateMessage ? Action.GET_PM_INBOX : Action.GET_INBOX;
    }

    @Override
    protected @NotNull String getValue3() {
        return String.valueOf(messageID);
    }
}
