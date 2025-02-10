package net.juligames.goodproxy.util;

import org.jetbrains.annotations.NotNull;

public record Credentials(@NotNull String username, @NotNull String password) {
}
