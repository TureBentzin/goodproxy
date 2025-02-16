package net.juligames.goodproxy.web.schema;


import io.swagger.v3.oas.annotations.media.Schema;

/**
 * @author Ture Bentzin
 * @since 16-02-2025
 */
@Schema(description = "The number of unread messages in the inbox")
public record InboxIndex(
        @Schema(description = "The number of unread messages in the inbox")
        int amount,
        @Schema boolean privateMessages
) {
}
