package net.juligames.goodproxy.prx;

import net.juligames.goodproxy.util.Credentials;
import net.juligames.goodproxy.websoc.BankingAPI;
import net.juligames.goodproxy.websoc.WebsocketManager;
import net.juligames.goodproxy.websoc.command.v1.response.EchoResponse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.UUID;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class StagingTest {

    private static final @NotNull WebsocketManager websocketManager = new WebsocketManager();
    private static final @NotNull Logger LOGGER = LogManager.getLogger(StagingTest.class);


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

    @Test
    void staging() {

        String message0 = UUID.randomUUID().toString();
        String message1 = UUID.randomUUID().toString();
        String message2 = UUID.randomUUID().toString();
        String message3 = UUID.randomUUID().toString();
        String message4 = UUID.randomUUID().toString();

        LOGGER.info("Echo 0 : {}", message0);
        LOGGER.info("Echo 1 : {}", message1);
        LOGGER.info("Echo 2 : {}", message2);
        LOGGER.info("Echo 3 : {}", message3);
        LOGGER.info("Echo 4 : {}", message4);


        Future<EchoResponse> echo0 = proxyAPI.echo(message0);
        Future<EchoResponse> echo1 = proxyAPI.echo(message1);
        Future<EchoResponse> echo2 = proxyAPI.echo(message2);
        Future<EchoResponse> echo3 = proxyAPI.echo(message3);
        Future<EchoResponse> echo4 = proxyAPI.echo(message4);

        //join

        try {
            EchoResponse echoResponse0 = echo0.get();
            EchoResponse echoResponse1 = echo1.get();
            EchoResponse echoResponse2 = echo2.get();
            EchoResponse echoResponse3 = echo3.get();
            EchoResponse echoResponse4 = echo4.get();

            LOGGER.info("Echo R0: {}", echoResponse0.getValue1());
            LOGGER.info("Echo R1: {}", echoResponse1.getValue1());
            LOGGER.info("Echo R2: {}", echoResponse2.getValue1());
            LOGGER.info("Echo R3: {}", echoResponse3.getValue1());
            LOGGER.info("Echo R4: {}", echoResponse4.getValue1());

            assertEquals(message0, echoResponse0.getValue1());
            assertEquals(message1, echoResponse1.getValue1());
            assertEquals(message2, echoResponse2.getValue1());
            assertEquals(message3, echoResponse3.getValue1());
            assertEquals(message4, echoResponse4.getValue1());

        } catch (ExecutionException | InterruptedException e) {
            throw new RuntimeException(e);
        }

    }
}
