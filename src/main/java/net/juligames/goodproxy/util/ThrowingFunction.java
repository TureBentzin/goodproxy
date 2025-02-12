package net.juligames.goodproxy.util;

import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;
import java.util.function.Function;

public interface ThrowingFunction<T, R, E extends Throwable> {

    static <T, R> @NotNull ThrowingFunction<T, R, RuntimeException> of(@NotNull Function<T, R> function) {
        return function::apply;
    }

    @NotNull R apply(@NotNull T r) throws E;

    default @NotNull R runOrCatch(@NotNull T t, @NotNull Function<E, R> onError) {
        try {
            return apply(t);
        } catch (Throwable e) {
            //noinspection unchecked - We know that the Throwable is of type E
            E e1 = (E) e;
            return onError.apply(e1);
        }
    }
}
