package net.juligames.goodproxy.websoc.command.v1;

import net.juligames.goodproxy.prx.ProxyAPI;
import net.juligames.goodproxy.prx.ProxyAPIFactory;
import net.juligames.goodproxy.websoc.BankingAPI;
import net.juligames.goodproxy.websoc.WebsocketManager;
import net.juligames.goodproxy.websoc.command.v1.request.MOTDCommand;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Test;

import java.util.concurrent.ExecutionException;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Ture Bentzin
 * @since 08-02-2025
 */
class MOTDCommandTest {

    private static final @NotNull WebsocketManager websocketManager = new WebsocketManager();
    private static final @NotNull Logger LOGGER = LogManager.getLogger(MOTDCommandTest.class);

    @Test
    void motd() {
        ProxyAPI api = ProxyAPIFactory.create();
        try {
            String motd = api.motd().get();
            LOGGER.info("MOTD: {}", motd);
            assertNotNull(motd);
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }

    }
}