package net.juligames.goodproxy.websoc.command.v1.request;

import net.juligames.goodproxy.util.Credentials;
import net.juligames.goodproxy.websoc.action.Action;
import org.jetbrains.annotations.NotNull;

public class BalanceCommand extends AuthenticatedCommand {
    public BalanceCommand(@NotNull Credentials credentials) {
        super(credentials);
    }

    @Override
    @NotNull Action getAction() {
        return Action.GET_BALANCE;
    }
}
