package net.juligames.goodproxy.websoc.command.v1.response;

import net.juligames.goodproxy.displaymessage.DisplayMessage;
import net.juligames.goodproxy.displaymessage.source.HardcodeMessages;
import net.juligames.goodproxy.websoc.action.Action;
import net.juligames.goodproxy.websoc.command.APIMessage;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class DisplayMessageResponse extends Response {


    protected DisplayMessageResponse(@NotNull APIMessage source) {
        super(source);
    }

    public @NotNull DisplayMessage getMessage(@NotNull String messageSet) {
        String key = Objects.requireNonNull(getSource().getValue1());
        return new DisplayMessage(
                HardcodeMessages.getMessage(key, messageSet),
                key,
                messageSet
        );
    }

    @Override
    public @NotNull Action getAction() {
        return Action.DISPLAY_MESSAGE;
    }
}
