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


}
