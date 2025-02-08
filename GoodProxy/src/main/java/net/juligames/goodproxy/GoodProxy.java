package net.juligames.goodproxy;


import net.juligames.goodproxy.websoc.WebsocketManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;

import java.net.http.WebSocket;

/**
 * @author Ture Bentzin
 * @since 08-02-2025
 */
public class GoodProxy {

    protected static final @NotNull Logger LOGGER = LogManager.getLogger(GoodProxy.class);

    public static void main(@NotNull String @NotNull [] args) {

        LOGGER.info("Hello World!");

        WebsocketManager websocketManager = new WebsocketManager();
        boolean success = websocketManager.openNewConnection();
    }
}