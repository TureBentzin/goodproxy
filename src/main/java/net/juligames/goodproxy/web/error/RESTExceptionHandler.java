package net.juligames.goodproxy.web.error;

import org.checkerframework.checker.units.qual.N;
import org.jetbrains.annotations.NotNull;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;


@RestControllerAdvice
public class RESTExceptionHandler {

    @ExceptionHandler(RESTException.class)
    public @NotNull ResponseEntity<RESTErrorResponse> handleException(@NotNull final RESTException e) {
        return toResponseEntity(e.getError(), e);
    }

    @ExceptionHandler(TimeoutException.class)
    public @NotNull ResponseEntity<RESTErrorResponse> handleException(@NotNull final TimeoutException e) {
        return toResponseEntity(RESTError.TIMEOUT, e);
    }

    @ExceptionHandler(ExecutionException.class)
    public @NotNull ResponseEntity<RESTErrorResponse> handleException(@NotNull final ExecutionException e) {
        return toResponseEntity(RESTError.EXECUTION_FAILED, e);
    }

    @ExceptionHandler(InterruptedException.class)
    public @NotNull ResponseEntity<RESTErrorResponse> handleException(@NotNull final InterruptedException e) {
        return toResponseEntity(RESTError.THREAD_INTERRUPTED, e);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public @NotNull ResponseEntity<RESTErrorResponse> handleException(@NotNull final IllegalArgumentException e) {
        return toResponseEntity(RESTError.INVALID_ARGUMENT, e);
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public @NotNull ResponseEntity<RESTErrorResponse> handleException(@NotNull final HttpRequestMethodNotSupportedException e) {
        return toResponseEntity(RESTError.METHOD_NOT_ALLOWED, e);
    }

    @ExceptionHandler(ServletRequestBindingException.class)
    public @NotNull ResponseEntity<RESTErrorResponse> handleException(@NotNull final ServletRequestBindingException e) {
        return toResponseEntity(RESTError.INVALID_ARGUMENT, e);
    }

    @ExceptionHandler(Exception.class)
    public @NotNull ResponseEntity<RESTErrorResponse> handleException(@NotNull final Exception e) {
        return toResponseEntity(RESTError.UNEXPECTED, e);
    }


    private @NotNull ResponseEntity<RESTErrorResponse> toResponseEntity(@NotNull final RESTError error, @NotNull Exception e) {
        return ResponseEntity.status(error.getCode()).body(RESTErrorResponse.of(e, error));
    }


    public record RESTErrorResponse(
            @NotNull String message,
            @NotNull StatusDetails status,
            @NotNull String description) {

        public static @NotNull RESTErrorResponse of(@NotNull Exception e, @NotNull RESTError error) {
            String message = e.getMessage();
            if (message == null) message = "";

            return new RESTErrorResponse(
                    message,
                    new StatusDetails(error.getCode()),
                    error.getDescription()
            );
        }

        public record StatusDetails(int code, @NotNull String text) {
            public StatusDetails(@NotNull HttpStatus status) {
                this(status.value(), status.getReasonPhrase());
            }
        }
    }
}
