package net.juligames.goodproxy.web.error;

import jakarta.validation.constraints.Null;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class RESTException extends RuntimeException {

    private final @NotNull RESTError error;

    public RESTException(@NotNull RESTError error) {
        this.error = error;
    }

    public RESTException(@NotNull RESTError error, @Nullable String message) {
        super(message);
        this.error = error;
    }

    public @NotNull RESTError getError() {
        return error;
    }

}
