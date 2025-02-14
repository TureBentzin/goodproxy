package net.juligames.goodproxy.web;

import net.juligames.goodproxy.web.error.APIException;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.Callable;
import java.util.function.Supplier;

public record WebResponse<T>(long timestamp, T data) {

    @Contract("_ -> new")
    public static <T> @NotNull WebResponse<T> from(@NotNull T data) {
        return new WebResponse<>(System.currentTimeMillis(), data);
    }

    @Contract("_ -> new")
    public static <T> @NotNull WebResponse<T> from(@NotNull Callable<T> data) {
        long millis = System.currentTimeMillis();
        T result;
        try {
            result = data.call();
        } catch (Exception e) {
            throw new APIException(e);
        }
        return new WebResponse<>(millis, result); //maybe blocking
    }
}
