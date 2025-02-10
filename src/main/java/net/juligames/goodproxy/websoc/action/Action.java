package net.juligames.goodproxy.websoc.action;


import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * @author Ture Bentzin
 * @since 08-02-2025
 */
public enum Action {
    AUTHENTICATE,
    REGISTER,
    LOGOUT,
    PAY,
    GET_INBOX,
    GET_PM_INBOX,
    READ_INBOX,
    READ_PM_INBOX,
    GET_BALANCE,
    SEND_PM,

    MOTD, // Response and Request

    DISPLAY_MESSAGE("displayMessage"), // Response and Request
    INBOX,
    PM_INBOX;


    private final @NotNull String action;

    Action() {
        this.action = name().toLowerCase();
    }

    Action(final @NotNull String action) {
        this.action = action;
    }

    public @NotNull String getActionString() {
        return action;
    }

    public static @Nullable Action fromString(@NotNull String action) {
        for (Action a : values()) {
            if (a.action.equals(action)) {
                return a;
            }
        }
        for (Action a : values()) {
            if (a.name().toLowerCase().equals(action)) {
                return a;
            }
        }
        return null;
    }
}
