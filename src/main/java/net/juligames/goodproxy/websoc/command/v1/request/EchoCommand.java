package net.juligames.goodproxy.websoc.command.v1.request;

import net.juligames.goodproxy.websoc.action.Action;
import net.juligames.goodproxy.websoc.command.APIMessage;
import net.juligames.goodproxy.websoc.command.Command;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public class EchoCommand implements Command {

    private final @Nullable String value1;
    private final @Nullable String value2;
    private final @Nullable String value3;
    private final @Nullable String value4;

    public EchoCommand(@Nullable String value1, @Nullable String value2, @Nullable String value3, @Nullable String value4) {
        this.value1 = value1;
        this.value2 = value2;
        this.value3 = value3;
        this.value4 = value4;
    }

    public EchoCommand(@Nullable String value1, @Nullable String value2, @Nullable String value3) {
        this(value1, value2, value3, null);
    }

    public EchoCommand(@Nullable String value1, @Nullable String value2) {
        this(value1, value2, null, null);
    }

    public EchoCommand(@Nullable String value1) {
        this(value1, null, null, null);
    }

    public EchoCommand() {
        this(UUID.randomUUID().toString(), null, null, null);
    }


    @Override
    public @NotNull APIMessage pack() {
        return APIMessage.create(Action.ECHO, value1, value2, value3, value4);
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
}
