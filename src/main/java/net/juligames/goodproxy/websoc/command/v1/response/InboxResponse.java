package net.juligames.goodproxy.websoc.command.v1.response;

import net.juligames.goodproxy.websoc.action.Action;
import net.juligames.goodproxy.websoc.command.APIMessage;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class InboxResponse extends Response {

    private final int messageID;

    private final boolean privateMessage;

    private final @NotNull String message;


    protected InboxResponse(@NotNull APIMessage source) {
        super(source);
        assert source.getAction() == Action.INBOX || source.getAction() == Action.PM_INBOX;
        messageID = Integer.parseInt(Objects.requireNonNull(source.getValue1(), "Message ID is null"));
        message = Objects.requireNonNull(source.getValue2(), "Message is null");
        this.privateMessage = source.getAction() == Action.GET_PM_INBOX;
    }

    public int getMessageID() {
        return messageID;
    }

    public @NotNull String getMessage() {
        return message;
    }

    public boolean isPrivateMessage() {
        return privateMessage;
    }

    @Override
    public @NotNull String toString() {
        return "(" + messageID + ") " + message;
    }
}
