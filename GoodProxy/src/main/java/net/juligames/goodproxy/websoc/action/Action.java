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
    MOTD;


    private final @NotNull String action;

    Action() {
        this.action = name().toLowerCase();
    }

    Action(final @NotNull String action) {
        this.action = action.toLowerCase();
    }

    public @NotNull String getActionString() {
        return action;
    }
}
