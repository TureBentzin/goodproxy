package net.juligames.goodproxy.websoc;


import jakarta.websocket.ClientEndpoint;
import jakarta.websocket.OnMessage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;

/**
 * @author Ture Bentzin
 * @since 08-02-2025
 */

@ClientEndpoint
public class BankClient {


    private final @NotNull Logger LOGGER = LogManager.getLogger();

    @OnMessage
    public void onMessage(@NotNull String message) {
        LOGGER.info("Received message: {}", message);
    }

}
