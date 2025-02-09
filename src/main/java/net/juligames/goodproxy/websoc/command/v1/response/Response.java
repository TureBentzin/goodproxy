package net.juligames.goodproxy.websoc.command.v1.response;


import net.juligames.goodproxy.websoc.action.Action;
import net.juligames.goodproxy.websoc.command.APIMessage;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

/**
 * Will represent individual responses build from an APIMessage
 *
 * @author Ture Bentzin
 * @since 08-02-2025
 */
public abstract class Response {

    protected Response(@NotNull APIMessage source) {
        this.source = source;
    }

    @Contract("_ -> new")
    public static @NotNull Response fromMessage(@NotNull APIMessage apiMessage) {
        //Main response logik (factory method)

        //MOTD reponse
        //noinspection SwitchStatementWithTooFewBranches
        switch (apiMessage.getAction()) {
            case MOTD -> {
                return new MOTDResponse(apiMessage);
            }

            default -> throw new IllegalArgumentException("Unknown response!");
        }
    }

    private final @NotNull APIMessage source;

    protected final @NotNull APIMessage getSource() {
        return source;
    }

    public abstract @NotNull Action getAction();
}
