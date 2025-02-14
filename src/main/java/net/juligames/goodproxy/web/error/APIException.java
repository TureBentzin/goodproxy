package net.juligames.goodproxy.web.error;

import org.jetbrains.annotations.NotNull;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.INTERNAL_SERVER_ERROR)
public class APIException extends RuntimeException {
    public APIException(@NotNull String message) {
        super(message);
    }

    public APIException(@NotNull Throwable cause) {
        super(cause.getMessage(), cause);
    }
}
