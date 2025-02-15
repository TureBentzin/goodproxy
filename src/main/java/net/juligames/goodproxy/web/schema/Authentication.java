package net.juligames.goodproxy.web.schema;

import io.swagger.v3.oas.annotations.media.Schema;
import org.jetbrains.annotations.NotNull;

@Schema(description = "Authentication response")
public record Authentication(
      @Schema(description = "JSON Web Token")  String jwt,
      @Schema(description = "Message from the server") String message
) {
}
