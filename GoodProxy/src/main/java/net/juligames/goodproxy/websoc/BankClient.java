package net.juligames.goodproxy.websoc;


import jakarta.websocket.*;
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

    @OnOpen
    public void onOpen() {
        LOGGER.info("Connection opened");
    }


    @OnMessage
    public void onMessage(@NotNull String message) {
        LOGGER.info("Received message: {}", message);
    }

    @OnClose
    public void onClose() {
        LOGGER.info("Connection closed");
    }

    @OnError
    public void onError(@NotNull Throwable t) {
        LOGGER.error("Error", t);
    }

}
