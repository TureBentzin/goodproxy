package net.juligames.goodproxy.web.schema;


import net.juligames.goodproxy.websoc.command.v1.response.InboxResponse;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * @author Ture Bentzin
 * @since 16-02-2025
 */
public record InboxMessage(
        int id,
        @Nullable String sender,
        @NotNull String message,
        boolean privateMessage
) {

    @Contract("_ -> new")
    public static @NotNull InboxMessage from(@NotNull InboxResponse response) {
        String sender = null;

        if (response.isPrivateMessage()) {
            int colonIndex = response.getMessage().indexOf(':');
            if (colonIndex != -1) {
                sender = response.getMessage().substring(0, colonIndex);
            }
        }

        return new InboxMessage(response.getMessageID(), sender, response.getMessage(), response.isPrivateMessage());

    }
}
