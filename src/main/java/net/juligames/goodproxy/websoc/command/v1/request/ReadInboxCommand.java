package net.juligames.goodproxy.websoc.command.v1.request;

import net.juligames.goodproxy.util.Credentials;
import net.juligames.goodproxy.websoc.action.Action;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ReadInboxCommand extends AuthenticatedCommand {

    private final int messageID;
    private final boolean privateMessages;

    public ReadInboxCommand(@NotNull Credentials credentials, int messageID, boolean privateMessages) {
        super(credentials);
        this.messageID = messageID;
        this.privateMessages = privateMessages;
    }


    @Override
    @NotNull Action getAction() {
        return privateMessages ? Action.READ_PM_INBOX : Action.READ_INBOX;
    }

    public boolean isPrivateMessages() {
        return privateMessages;
    }

    @Override
    protected @Nullable String getValue3() {
        return String.valueOf(messageID);
    }
}
