package net.juligames.goodproxy.web.error;

import org.jetbrains.annotations.NotNull;
import org.springframework.http.HttpStatus;

public enum RESTError {
    EXAMPLE(HttpStatus.INTERNAL_SERVER_ERROR, "This is an example error"),
    UNEXPECTED(HttpStatus.INTERNAL_SERVER_ERROR, "An unexpected error occurred"),
    TIMEOUT(HttpStatus.REQUEST_TIMEOUT, "The request timed out"),
    THREAD_INTERRUPTED(HttpStatus.INTERNAL_SERVER_ERROR, "The thread was interrupted"),
    EXECUTION_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "The execution failed"),
    INVALID_ARGUMENT(HttpStatus.BAD_REQUEST, "An invalid argument was provided"),
    INVALID_CREDS(HttpStatus.BAD_REQUEST, "Invalid credentials were provided"),
    NOT_FOUND(HttpStatus.NOT_FOUND, "The requested resource was not found"),
    FORBIDDEN(HttpStatus.FORBIDDEN, "Access to the requested resource is forbidden"),
    UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "Access to the requested resource is unauthorized"),
    METHOD_NOT_ALLOWED(HttpStatus.METHOD_NOT_ALLOWED, "The requested method is not allowed"),
    ;
    private final @NotNull HttpStatus code;
    private final @NotNull String description;

    RESTError(@NotNull HttpStatus code, @NotNull String description) {
        this.code = code;
        this.description = description;
    }

    public @NotNull String getDescription() {
        return description;
    }

    public @NotNull HttpStatus getCode() {
        return code;
    }
}
