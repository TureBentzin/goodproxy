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
        switch (apiMessage.getAction()) {
            case MOTD -> {
                return new MOTDResponse(apiMessage);
            }
            case DISPLAY_MESSAGE -> {
                return new DisplayMessageResponse(apiMessage);
            }
            case INBOX, PM_INBOX -> {
                return new InboxResponse(apiMessage);
            }
            case ECHO -> {
                return new EchoResponse(apiMessage);
            }

            default -> throw new IllegalArgumentException("Unknown response!");
        }
    }

    private final @NotNull APIMessage source;

    public final @NotNull APIMessage getSource() {
        return source;
    }

    public @NotNull Action getAction() {
        return source.getAction();
    }

    /**
     * Override this if the message may come unexpected.
     * And should therefore not complete any futures, but be stored for polling
     */
    @SuppressWarnings("SameReturnValue")
    public boolean probablyUnexpected() {
        return false;
    }

    @Override
    public @NotNull String toString() {
        return source.getCommandString();
    }
}
