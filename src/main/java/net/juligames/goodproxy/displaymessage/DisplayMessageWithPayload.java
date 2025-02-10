package net.juligames.goodproxy.displaymessage;

import org.jetbrains.annotations.NotNull;

import java.util.function.Function;

public class DisplayMessageWithPayload<T> extends DisplayMessage {

    private final @NotNull String rawPayload;
    private final @NotNull T payload;

    public DisplayMessageWithPayload(@NotNull String message, @NotNull String key, @NotNull String messageSet, @NotNull String rawPayload, @NotNull Function<String, T> payloadConverter) {
        super(message, key, messageSet);
        this.rawPayload = rawPayload;
        this.payload = payloadConverter.apply(rawPayload);
    }

    public @NotNull String getRawPayload() {
        return rawPayload;
    }

    @Deprecated
    public int getPayloadAsInteger() {
        return Integer.parseInt(rawPayload);
    }

    @Deprecated
    public boolean getPayloadAsBoolean() {
        return Boolean.parseBoolean(rawPayload);
    }

    @Deprecated
    public double getPayloadAsDouble() {
        return Double.parseDouble(rawPayload);
    }

    public @NotNull T getPayload() {
        return payload;
    }

    @Override
    public @NotNull String toString() {
        //super.toString, but replace {} with payload
        return super.toString().replace("{}", getRawPayload());
    }
}