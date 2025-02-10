package net.juligames.goodproxy.websoc.command.v1.request;

import net.juligames.goodproxy.util.Credentials;
import net.juligames.goodproxy.websoc.action.Action;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Range;

public class PayCommand extends AuthenticatedCommand {

    private final @NotNull String destination;

    @Range(from = 1, to = Integer.MAX_VALUE)
    private final int amount;

    public PayCommand(@NotNull Credentials credentials, @NotNull String destination, @Range(from = 1, to = Integer.MAX_VALUE) int amount) {
        super(credentials);
        this.destination = destination;
        this.amount = amount;
    }

    @Override
    @NotNull Action getAction() {
        return Action.PAY;
    }

    @Override
    protected @Nullable String getValue3() {
        return destination;
    }

    @Override
    protected @Nullable String getValue4() {
        return String.valueOf(amount);
    }
}
