package net.juligames.goodproxy.websoc.command.v1.request;

import net.juligames.goodproxy.displaymessage.DisplayMessage;
import net.juligames.goodproxy.prx.ProxyAPI;
import net.juligames.goodproxy.prx.ProxyAPIFactory;
import net.juligames.goodproxy.util.Credentials;
import net.juligames.goodproxy.websoc.WebsocketManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Test;

import java.util.UUID;
import java.util.concurrent.ExecutionException;

import static org.junit.jupiter.api.Assertions.assertEquals;


class RegisterCommandTest {
    private static final @NotNull WebsocketManager websocketManager = new WebsocketManager();
    private static final @NotNull Logger LOGGER = LogManager.getLogger(MOTDCommandTest.class);

    @Test
    void register() {
        ProxyAPI api = ProxyAPIFactory.create();
        try {
            String username = UUID.randomUUID().toString();
            String password = UUID.randomUUID().toString();

            DisplayMessage displayMessage = api.register(new Credentials(username, password)).get();
            LOGGER.info(displayMessage);
            assertEquals("register.success", displayMessage.key());
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }
    }
}