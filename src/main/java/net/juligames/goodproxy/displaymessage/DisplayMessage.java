package net.juligames.goodproxy.displaymessage;

import org.jetbrains.annotations.NotNull;
import java.util.Objects;

public class DisplayMessage {
    private final @NotNull String message;
    private final @NotNull String key;
    private final @NotNull String messageSet;

    public DisplayMessage(@NotNull String message, @NotNull String key, @NotNull String messageSet) {
        this.message = message;
        this.key = key;
        this.messageSet = messageSet;
    }

    @Override
    public @NotNull String toString() {
        return message;
    }

    public @NotNull String message() {
        return message;
    }

    public @NotNull String key() {
        return key;
    }

    public @NotNull String messageSet() {
        return messageSet;
    }

    @Override
    public boolean equals(@NotNull Object obj) {
        if (obj == this) return true;
        if (obj.getClass() != this.getClass()) return false;
        var that = (DisplayMessage) obj;
        return Objects.equals(this.message, that.message) &&
                Objects.equals(this.key, that.key) &&
                Objects.equals(this.messageSet, that.messageSet);
    }

    @Override
    public int hashCode() {
        return Objects.hash(message, key, messageSet);
    }

}
