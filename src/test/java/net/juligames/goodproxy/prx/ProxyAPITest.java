package net.juligames.goodproxy.prx;

import net.juligames.goodproxy.displaymessage.DisplayMessage;
import net.juligames.goodproxy.displaymessage.DisplayMessageWithPayload;
import net.juligames.goodproxy.util.Credentials;
import net.juligames.goodproxy.websoc.WebsocketManager;
import net.juligames.goodproxy.websoc.command.v1.response.InboxResponse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.*;

import java.io.IOException;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

import static org.junit.jupiter.api.Assertions.*;

@SuppressWarnings("LoggingSimilarMessage")
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

    @AfterEach
    void runJanitor() {
        proxyAPI.janitor();
    }


    @Order(1)
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
            assertEquals(1000, balance);
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }
        LOGGER.traceExit();
    }

    @Order(5)
    @Test
    void payToUnknownDestination() {
        LOGGER.traceEntry();
        try {
            DisplayMessage displayMessage = proxyAPI.pay(credentials, "invalid_ " + UUID.randomUUID(), 100).get();
            LOGGER.info(displayMessage);
            assertEquals("pay.fail.unknown_dest", displayMessage.key());
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }
        LOGGER.traceExit();
    }

    @Order(6)
    @Test
    void payFromUnknownSender() {
        LOGGER.traceEntry();
        try {
            Credentials unknownCredentials = new Credentials("invalid_" + UUID.randomUUID(), UUID.randomUUID().toString());
            DisplayMessage displayMessage = proxyAPI.pay(unknownCredentials, credentials.username(), 100).get();
            LOGGER.info(displayMessage);
            assertEquals("pay.fail.orig_user_unknown", displayMessage.key());
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }
        LOGGER.traceExit();
    }

    @Order(7)
    @Test
    void payNegativeAmount() {
        final Credentials partnerCredentials = createPartner();
        LOGGER.traceEntry();
        try {
            DisplayMessage displayMessage = proxyAPI.pay(credentials, partnerCredentials.username(), -100).get();
            LOGGER.info(displayMessage);
            assertEquals("pay.fail.negative_amount", displayMessage.key());
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }
        LOGGER.traceExit();
    }

    @Order(8)
    @Test
    void payWithInvalidCredentials() {
        //user exists, but password is wrong
        final Credentials partnerCredentials = createPartner();
        final Credentials target = createPartner();
        final Credentials invalidCredentials = new Credentials(partnerCredentials.username(), UUID.randomUUID().toString());
        LOGGER.traceEntry();
        try {
            DisplayMessage displayMessage = proxyAPI.pay(invalidCredentials, target.username(), 100).get();
            LOGGER.info(displayMessage);
            assertEquals("pay.fail.credentials", displayMessage.key());
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }
        LOGGER.traceExit();
    }

    @Order(9)
    @Test
    void payLackingFunds() {
        final Credentials partnerCredentials = createPartner();
        LOGGER.traceEntry();
        try {
            DisplayMessage displayMessage = proxyAPI.pay(credentials, partnerCredentials.username(), 1001).get();
            LOGGER.info(displayMessage);
            assertEquals("pay.fail.not_enough_money", displayMessage.key());
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }
        LOGGER.traceExit();
    }

    @Order(10)
    @Test
    void pay() {
        final Credentials partnerCredentials = createPartner();
        LOGGER.traceEntry();
        try {
            DisplayMessage displayMessage = proxyAPI.pay(credentials, partnerCredentials.username(), 1000).get();
            LOGGER.info(displayMessage);
            assertEquals("pay.success", displayMessage.key());

            DisplayMessageWithPayload<Double> messageWithPayload = proxyAPI.balance(credentials).get();
            LOGGER.info(messageWithPayload);
            assertEquals("balance.success", messageWithPayload.key());
            double balance = messageWithPayload.getPayload();
            assertEquals(0.0, balance);
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }

        LOGGER.traceExit();
    }

    @Order(11)
    @Test
    void getInbox() {
        LOGGER.traceEntry();
        try {
            DisplayMessageWithPayload<Integer> displayMessage = proxyAPI.getInbox(credentials).get();
            assertEquals("", displayMessage.message());
            assertEquals("message_count", displayMessage.key());
            int inboxSize = displayMessage.getPayload();
            LOGGER.info("Inbox size:  {}", inboxSize);
            assertEquals(1, inboxSize);
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }
        LOGGER.traceExit();
    }

    @Order(12)
    @Test
    void getInboxMessage() {
        LOGGER.traceEntry();
        try {
            DisplayMessageWithPayload<Integer> displayMessage = proxyAPI.getInbox(credentials).get();
            assertEquals("message_count", displayMessage.key());
            assertEquals("", displayMessage.message());
            int inboxSize = displayMessage.getPayload();
            assertEquals(1, inboxSize);
            InboxResponse inboxResponse = proxyAPI.getInboxMessage(credentials, 1).get();
            int messageID = inboxResponse.getMessageID();
            assertEquals(1, messageID);
            LOGGER.info(inboxResponse);
            assertEquals(1, messageID);
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }
        LOGGER.traceExit();
    }

    @Order(13)
    @Test
    void getPrivateInbox() {
        LOGGER.traceEntry();
        try {
            DisplayMessageWithPayload<Integer> displayMessage = proxyAPI.getInbox(credentials, true).get();
            assertEquals("", displayMessage.message());
            assertEquals("pm_message_count", displayMessage.key());
            int inboxSize = displayMessage.getPayload();
            LOGGER.info("Inbox size:  {}", inboxSize);
            assertEquals(1, inboxSize);
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }
        LOGGER.traceExit();
    }

    @Order(14)
    @Test
    void getPrivateInboxMessage() {
        LOGGER.traceEntry();
        try {
            DisplayMessageWithPayload<Integer> displayMessage = proxyAPI.getInbox(credentials, true).get();
            assertEquals("pm_message_count", displayMessage.key());
            assertEquals("", displayMessage.message());
            int inboxSize = displayMessage.getPayload();
            assertEquals(1, inboxSize);
            InboxResponse inboxResponse = proxyAPI.getInboxMessage(credentials, 1, true).get();
            int messageID = inboxResponse.getMessageID();
            assertEquals(1, messageID);
            LOGGER.info(inboxResponse);
            assertEquals(1, messageID);
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }
        LOGGER.traceExit();
    }

    @Order(15)
    @Test
    @Disabled("Server closes connection after reading a message")
    void readInbox() {
        LOGGER.traceEntry();
        try {
            DisplayMessage displayMessage = proxyAPI.readInbox(credentials, 1).get();
            LOGGER.info(displayMessage);
            assertEquals("read_inbox.success", displayMessage.key());

            DisplayMessageWithPayload<Integer> messageWithPayload = proxyAPI.getInbox(credentials).get();
            assertEquals("message_count", messageWithPayload.key());
            assertEquals("", messageWithPayload.message());
            int inboxSize = messageWithPayload.getPayload();
            assertEquals(0, inboxSize);
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }
        LOGGER.traceExit();
    }

    @Order(16)
    @Test
    @Disabled("Server closes connection after reading a private message")
    void readPrivateInbox() {
        LOGGER.traceEntry();
        try {
            DisplayMessage displayMessage = proxyAPI.readPrivateInbox(credentials, 1).get();
            LOGGER.info(displayMessage);
            assertEquals("read_pm_inbox.success", displayMessage.key());

            DisplayMessageWithPayload<Integer> messageWithPayload = proxyAPI.getInbox(credentials, true).get();
            assertEquals("pm_message_count", messageWithPayload.key());
            assertEquals("", messageWithPayload.message());
            int inboxSize = messageWithPayload.getPayload();
            assertEquals(0, inboxSize);
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }
        LOGGER.traceExit();
    }

    @Order(1000) // ALWAYS LAST
    @Test
    void logout() {
        LOGGER.traceEntry();
        try {
            DisplayMessage displayMessage = proxyAPI.logout(credentials).get();
            LOGGER.info(displayMessage);
            assertEquals("logout.success", displayMessage.key());
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }
        LOGGER.traceExit();
    }


    @AfterAll
    static void tearDown() {
        try {
            proxyAPI.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    private @NotNull Credentials createPartner() {
        Credentials partnerCredentials = new Credentials("partner_" + UUID.randomUUID(), UUID.randomUUID().toString());
        try {
            DisplayMessage displayMessage = proxyAPI.register(partnerCredentials).get();
            assertEquals("register.success", displayMessage.key());
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }
        return partnerCredentials;
    }


}