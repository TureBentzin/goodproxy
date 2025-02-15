package net.juligames.goodproxy.util;

import io.swagger.v3.oas.annotations.media.Schema;
import org.jetbrains.annotations.NotNull;

@Schema(description = "Login credentials for authentication")
public record Credentials(
        @Schema(description = "Username for authentication", example = "user123", requiredMode = Schema.RequiredMode.REQUIRED)
        String username,

        @Schema(description = "Password for authentication", example = "password123", requiredMode = Schema.RequiredMode.REQUIRED)
        String password
) {
}