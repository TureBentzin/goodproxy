package net.juligames.goodproxy.websoc.command.v1.request;

import net.juligames.goodproxy.util.Credentials;
import net.juligames.goodproxy.websoc.action.Action;
import org.jetbrains.annotations.NotNull;

public class AuthenticateCommand extends AuthenticatedCommand{

    public AuthenticateCommand(final @NotNull Credentials credentials) {
        super(credentials);
    }

    @Override
    @NotNull Action getAction() {
        return Action.AUTHENTICATE;
    }
}
