package net.juligames.goodproxy.websoc.command.v1.response;


import net.juligames.goodproxy.websoc.action.Action;
import net.juligames.goodproxy.websoc.command.APIMessage;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

/**
 * @author Ture Bentzin
 * @since 08-02-2025
 */
public class MOTDResponse extends Response {

    private final @NotNull String motd;

    protected MOTDResponse(@NotNull APIMessage source) {
        super(source);
        @NotNull String v1 = Objects.requireNonNull(source.getValue1(), "MOTD is required");
        if (v1.isBlank()) {
            throw new IllegalArgumentException("MOTD cant be blank!");
        }
        this.motd = v1;
    }

    @Override
    public @NotNull Action getAction() {
        return Action.MOTD;
    }

    public @NotNull String getMOTD() {
        return motd;
    }
}
