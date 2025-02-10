package net.juligames.goodproxy.websoc.command.v1.response;

import net.juligames.goodproxy.websoc.action.Action;
import net.juligames.goodproxy.websoc.command.APIMessage;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class InboxResponse extends Response{

    private int messageID = -1;

    private @NotNull String message = "";


    public InboxResponse(@NotNull APIMessage source) {
        super(source);
        messageID = Integer.parseInt(Objects.requireNonNull(source.getValue1(), "Message ID is null"));
        message = Objects.requireNonNull(source.getValue2(), "Message is null");
    }

    public int getMessageID() {
        return messageID;
    }

    public @NotNull String getMessage() {
        return message;
    }

    @Override
    public @NotNull Action getAction() {
        return Action.INBOX;
    }

    @Override
    public @NotNull String toString() {
        return "(" + messageID + ") " + message;
    }
}
