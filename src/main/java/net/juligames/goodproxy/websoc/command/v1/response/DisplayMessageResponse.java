package net.juligames.goodproxy.websoc.command.v1.response;

import net.juligames.goodproxy.displaymessage.DisplayMessage;
import net.juligames.goodproxy.displaymessage.DisplayMessageWithPayload;
import net.juligames.goodproxy.displaymessage.source.HardcodeMessages;
import net.juligames.goodproxy.websoc.action.Action;
import net.juligames.goodproxy.websoc.command.APIMessage;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;
import java.util.function.Function;

public class DisplayMessageResponse extends Response {


    protected DisplayMessageResponse(@NotNull APIMessage source) {
        super(source);
    }

    public @NotNull DisplayMessage getMessage(@Nullable String messageSet) {
        String key = Objects.requireNonNull(getSource().getValue2(), "No key provided");
        return new DisplayMessage(
                (messageSet == null) ? Objects.requireNonNull(getSource().getValue1(), "No message provided") : HardcodeMessages.getMessage(key, messageSet),
                key,
                (messageSet == null) ? "" : messageSet
        );
    }

    public @NotNull <T> DisplayMessageWithPayload<T> getMessageWithPayload(@Nullable String messageSet, @NotNull Function<String, T> payloadConverter) {
        String key = Objects.requireNonNull(getSource().getValue2(), "No key provided");
        return new DisplayMessageWithPayload(
                (messageSet == null) ? Objects.requireNonNull(getSource().getValue1(), "No message provided") : HardcodeMessages.getMessage(key, messageSet),
                key,
                (messageSet == null) ? "" : messageSet,
                Objects.requireNonNull(getSource().getValue3(), "No payload provided"),
                payloadConverter
        );
    }

    @Override
    public @NotNull Action getAction() {
        return Action.DISPLAY_MESSAGE;
    }
}
