package net.juligames.goodproxy.websoc;


import jakarta.websocket.ContainerProvider;
import jakarta.websocket.Session;
import jakarta.websocket.WebSocketContainer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;

import java.net.URI;
import java.util.function.Consumer;

/**
 * @author Ture Bentzin
 * @since 08-02-2025
 */
public class WebsocketManager {

    private static final @NotNull Logger LOGGER = LogManager.getLogger(WebsocketManager.class);

    public boolean openNewConnection(@NotNull Consumer<Session> action) {
        WebSocketContainer container = ContainerProvider.getWebSocketContainer();
        //TODO config
        String uriString = BankingAPI.API_URL;
        boolean success = true;
        try {
            URI uri = new URI(uriString);
            try(final Session session =  container.connectToServer(BankClient.class, uri)) {
                action.accept(session);
            }
        } catch (Exception e) {
            LOGGER.error("Failed to open connection to {}", uriString, e);
            success = false;
        }

        return success;
    }
}
