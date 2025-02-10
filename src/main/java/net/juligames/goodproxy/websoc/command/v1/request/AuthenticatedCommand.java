package net.juligames.goodproxy.websoc.command.v1.request;

import net.juligames.goodproxy.util.Credentials;
import net.juligames.goodproxy.websoc.action.Action;
import net.juligames.goodproxy.websoc.command.APIMessage;
import net.juligames.goodproxy.websoc.command.Command;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public abstract class AuthenticatedCommand implements Command {


    private final @NotNull Credentials credentials;

    protected AuthenticatedCommand(@NotNull Credentials credentials) {
        this.credentials = credentials;
    }

    @Override
    public final @NotNull APIMessage pack() {
        return APIMessage.create(getAction(), credentials.username(), credentials.password(), getValue3(), getValue4());
    }

    abstract @NotNull Action getAction();

    protected @Nullable String getValue3() {
        return null;
    }

    protected @Nullable String getValue4() {
        return null;
    }

}
