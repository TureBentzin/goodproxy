package net.juligames.goodproxy.websoc.command.v1.response;

import net.juligames.goodproxy.displaymessage.DisplayMessage;
import net.juligames.goodproxy.displaymessage.DisplayMessageWithPayload;
import net.juligames.goodproxy.displaymessage.source.HardcodeMessages;
import net.juligames.goodproxy.websoc.action.Action;
import net.juligames.goodproxy.websoc.command.APIMessage;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.function.Function;

public class DisplayMessageResponse extends Response {

    public static final @NotNull Set<String> PROBABLY_UNEXPECTED_KEYS = Set.of("pay.received", "pm_inbox.received", "inbox.received");

    protected DisplayMessageResponse(@NotNull APIMessage source) {
        super(source);
        assert source.getAction() == Action.DISPLAY_MESSAGE;
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
        return new DisplayMessageWithPayload<>(
                (messageSet == null) ? Objects.requireNonNull(getSource().getValue1(), "No message provided") : HardcodeMessages.getMessage(key, messageSet),
                key,
                (messageSet == null) ? "" : messageSet,
                Objects.requireNonNull(getSource().getValue3(), "No payload provided"),
                payloadConverter
        );
    }

    @Override
    public boolean probablyUnexpected() {
        return PROBABLY_UNEXPECTED_KEYS.contains(getSource().getValue2());
    }

}
