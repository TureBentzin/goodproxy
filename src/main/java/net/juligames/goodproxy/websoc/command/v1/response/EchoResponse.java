package net.juligames.goodproxy.websoc.command.v1.response;

import net.juligames.goodproxy.websoc.command.APIMessage;
import net.juligames.goodproxy.websoc.command.v1.request.EchoCommand;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

public class EchoResponse extends Response {


    protected EchoResponse(@NotNull APIMessage source) {
        super(source);
    }

    public @Nullable String getValue1() {
        return getSource().getValue1();
    }

    public @Nullable String getValue2() {
        return getSource().getValue2();
    }

    public @Nullable String getValue3() {
        return getSource().getValue3();
    }

    public @Nullable String getValue4() {
        return getSource().getValue4();
    }

    public boolean isResponse(@NotNull EchoCommand request) {

        return Objects.equals(request.getValue1(), getValue1()) &&
                Objects.equals(request.getValue2(), getValue2()) &&
                Objects.equals(request.getValue3(), getValue3()) &&
                Objects.equals(request.getValue4(), getValue4());
    }
}
