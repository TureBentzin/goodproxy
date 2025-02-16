package net.juligames.goodproxy.web.schema;


import io.swagger.v3.oas.annotations.media.Schema;

/**
 * @author Ture Bentzin
 * @since 16-02-2025
 */
@Schema(description = "The result of a payment")
public record PayResult(
        String receiver,
        int amount,
        boolean success,
        String messageKey,
        String message
) {
}
