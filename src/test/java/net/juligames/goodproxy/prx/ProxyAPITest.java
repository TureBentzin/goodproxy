package net.juligames.goodproxy.prx;

import net.juligames.goodproxy.displaymessage.DisplayMessage;
import net.juligames.goodproxy.displaymessage.DisplayMessageWithPayload;
import net.juligames.goodproxy.util.Credentials;
import net.juligames.goodproxy.websoc.BankingAPI;
import net.juligames.goodproxy.websoc.WebsocketManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.*;

import java.util.UUID;
import java.util.concurrent.ExecutionException;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class ProxyAPITest {
    private static final @NotNull WebsocketManager websocketManager = new WebsocketManager();
    private static final @NotNull Logger LOGGER = LogManager.getLogger(ProxyAPITest.class);


    @SuppressWarnings("NullabilityAnnotations")
    private static ProxyAPI proxyAPI;

    private static @NotNull Credentials credentials = new Credentials("", "");

    @BeforeAll
    static void setup() {
        proxyAPI = ProxyAPIFactory.create();
    }

    @BeforeAll
    static void genCredentials() {
        credentials = new Credentials("test_" + UUID.randomUUID(), UUID.randomUUID().toString());
    }

    @Order(1)
    @Test
    void motd() {
        LOGGER.traceEntry();
        try {
            String motd = proxyAPI.motd().get();
            LOGGER.info("MOTD: {}", motd);
            assertNotNull(motd);
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }
        LOGGER.traceExit();
    }

    @Order(2)
    @Test
    void register() {
        LOGGER.traceEntry();
        try {
            DisplayMessage displayMessage = proxyAPI.register(credentials).get();
            LOGGER.info(displayMessage);
            assertEquals("register.success", displayMessage.key());
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }
        LOGGER.traceExit();
    }

    @Order(3)
    @Test
    void authenticate() {
        LOGGER.traceEntry();
        try {
            DisplayMessage displayMessage = proxyAPI.authenticate(credentials).get();
            LOGGER.info(displayMessage);
            assertEquals("auth.success", displayMessage.key());
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }
        LOGGER.traceExit();
    }

    @Order(4)
    @Test
    void balance() {
        LOGGER.traceEntry();
        try {
            DisplayMessageWithPayload<Double> displayMessage = proxyAPI.balance(credentials).get();
            LOGGER.info(displayMessage);
            assertEquals("balance.success", displayMessage.key());
            double balance = displayMessage.getPayload();
            LOGGER.info("Balance:  {}", balance);
            assertEquals(0.0, balance);
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }
        LOGGER.traceExit();
    }


    @AfterAll
    static void tearDown() {
        BankingAPI.unregister(proxyAPI.getSession().getId());
    }
}