package net.juligames.goodproxy.websoc.action;


import com.google.common.collect.EnumHashBiMap;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

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

    ECHO, // DEBUG

    MOTD, // Response and Request

    DISPLAY_MESSAGE("displayMessage"), // Response and Request
    INBOX,
    PM_INBOX;

    static final @NotNull EnumHashBiMap<Action, String> uniqueIdentifiers = EnumHashBiMap.create(Action.class);

    static {
        HashMap<String, Set<Action>> prefixMap = new HashMap<>();
        for (Action action : values()) {
            for (int i = 1; i <= action.action.length(); i++) {
                String prefix = action.action.substring(0, i);
                prefixMap.computeIfAbsent(prefix, k -> new HashSet<>()).add(action);
            }
        }

        for (Action action : values()) {
            for (int i = 1; i <= action.action.length(); i++) {
                String prefix = action.action.substring(0, i);
                if (prefixMap.get(prefix).size() == 1) {
                    uniqueIdentifiers.put(action, prefix.toUpperCase());
                    break;
                }
            }
        }
    }

    public static @Nullable Action fromUID(@NotNull String uid) {
        return uniqueIdentifiers.inverse().get(uid);
    }

    private final @NotNull String action;

    Action() {
        this.action = name().toLowerCase();
    }

    Action(@SuppressWarnings("SameParameterValue") final @NotNull String action) {
        this.action = action;
    }

    public @NotNull String getActionString() {
        return action;
    }

    /**
     * Unique identifier.
     * Consists of the minimum needed characters to identify the action from the other actions.
     * This is determined once and stored in a key-value structure statically.
     */
    public @NotNull String uid() {
        return Objects.requireNonNull(uniqueIdentifiers.get(this), "Invalid UID!");
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
        if (uniqueIdentifiers.containsValue(action)) {
            //avoid this
            return uniqueIdentifiers.inverse().get(action);
        }
        return null;
    }
}
