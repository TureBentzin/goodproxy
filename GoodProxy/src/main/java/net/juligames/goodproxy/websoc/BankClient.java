package net.juligames.goodproxy.websoc;

import jakarta.websocket.*;
import net.juligames.goodproxy.websoc.command.PackedCommand;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.ThreadContext;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * @author Ture Bentzin
 * @since 08-02-2025
 */
@ClientEndpoint
public class BankClient {

    private final @NotNull Logger LOGGER = LogManager.getLogger();
    private @Nullable Session session;

    protected @NotNull Session session() {
        if (session == null) {
            throw new IllegalStateException("Session not connected");
        }
        return session;
    }

    @OnOpen
    public void onOpen(@NotNull Session session) {
        this.session = session;
        ThreadContext.put("sessionId", session.getId()); // Store session ID in MDC
        LOGGER.info("Connected to {}", session.getRequestURI());
    }

    @OnMessage
    public void onMessage(@NotNull String message) {
        LOGGER.info("Received message: {}", message);
        PackedCommand packedCommand = PackedCommand.fromJson(message);
        BankingAPI.handleCommand(session(), packedCommand);
    }

    @OnClose
    public void onClose() {
        LOGGER.info("Connection closed");
        ThreadContext.remove("sessionId"); //
    }

    @OnError
    public void onError(@NotNull Throwable t) {
        LOGGER.error("Error", t);
    }
}
