package net.juligames.goodproxy.websoc.commands;


import net.juligames.goodproxy.websoc.action.Action;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.AccessFlag;

/**
 * @author Ture Bentzin
 * @since 08-02-2025
 */
public class PackedCommand {

    private final @NotNull Action action;

    private final @Nullable String value1;

    private final @Nullable String value2;

    private final @Nullable String value3;

    private final @Nullable String value4;


    private PackedCommand(@NotNull Action action, @Nullable String value1, @Nullable String value2, @Nullable String value3, @Nullable String value4) {
        this.action = action;
        this.value1 = value1;
        this.value2 = value2;
        this.value3 = value3;
        this.value4 = value4;
    }

    public static @NotNull PackedCommand create(@NotNull Action action) {
        return new PackedCommand(action, null, null, null, null);
    }

    public static @NotNull PackedCommand create(@NotNull Action action, @NotNull String value1) {
        return new PackedCommand(action, value1, null, null, null);
    }

    public static @NotNull PackedCommand create(@NotNull Action action, @NotNull String value1, @NotNull String value2) {
        return new PackedCommand(action, value1, value2, null, null);
    }

    public static @NotNull PackedCommand create(@NotNull Action action, @NotNull String value1, @NotNull String value2, @NotNull String value3) {
        return new PackedCommand(action, value1, value2, value3, null);
    }

    public static @NotNull PackedCommand create(@NotNull Action action, @NotNull String value1, @NotNull String value2, @NotNull String value3, @NotNull String value4) {
        return new PackedCommand(action, value1, value2, value3, value4);
    }

    public @NotNull Action getAction() {
        return action;
    }

    public @Nullable String getValue1() {
        return value1;
    }

    public @Nullable String getValue2() {
        return value2;
    }

    public @Nullable String getValue3() {
        return value3;
    }

    public @Nullable String getValue4() {
        return value4;
    }

    public @NotNull String getActionString() {
        return action.getActionString();
    }

    /**
     * This is for display purposes only
     *
     * @return the command string
     */
    public @NotNull String getCommandString() {
        StringBuilder builder = new StringBuilder();
        builder.append(action.getActionString());
        if (value1 != null) {
            builder.append(" ").append(value1);
        }
        if (value2 != null) {
            builder.append(" ").append(value2);
        }
        if (value3 != null) {
            builder.append(" ").append(value3);
        }
        if (value4 != null) {
            builder.append(" ").append(value4);
        }
        return builder.toString();
    }

    public @NotNull String toJson() {
        return "{\"action\":\"" + action.getActionString() + "\",\"value1\":\"" + (value1 == null ? "" : value1) + "\",\"value2\":\"" + (value2 == null ? "" : value2) + "\",\"value3\":\"" + (value3 == null ? "" : value3) + "\",\"value4\":\"" + (value4 == null ? "" : value4) + "\"}";
    }


}
