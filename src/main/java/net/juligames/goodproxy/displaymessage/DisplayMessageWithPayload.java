package net.juligames.goodproxy.displaymessage;

import org.jetbrains.annotations.NotNull;

public class DisplayMessageWithPayload extends DisplayMessage {

    private final @NotNull String payload;

    public DisplayMessageWithPayload(@NotNull String message, @NotNull String key, @NotNull String messageSet, @NotNull String payload) {
        super(message, key, messageSet);
        this.payload = payload;
    }

    public @NotNull String getPayload() {
        return payload;
    }

    public int getPayloadAsInteger() {
        return Integer.parseInt(payload);
    }

    public boolean getPayloadAsBoolean() {
        return Boolean.parseBoolean(payload);
    }

    public double getPayloadAsDouble() {
        return Double.parseDouble(payload);
    }

    @Override
    public @NotNull String toString() {
        //super.toString, but replace {} with payload
        return super.toString().replace("{}", getPayload());
    }
}