package net.juligames.goodproxy.displaymessage;

import org.jetbrains.annotations.NotNull;

public record DisplayMessage(String message, String key, String messageSet) {

    @Override
    public @NotNull String toString() {
        return message;
    }
}
