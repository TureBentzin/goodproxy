package net.juligames.goodproxy.web.error;

import org.jetbrains.annotations.NotNull;

public class RESTException extends RuntimeException {

    private final @NotNull RESTError error;

    public RESTException(@NotNull RESTError error) {
        this.error = error;
    }

    public @NotNull RESTError getError() {
        return error;
    }
}
