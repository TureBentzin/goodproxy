package net.juligames.goodproxy.api;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.juligames.goodproxy.util.Credentials;
import net.juligames.goodproxy.web.controller.AuthController;
import net.juligames.goodproxy.web.controller.ProtectedController;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;

import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Ture Bentzin
 * @since 16-02-2025
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class InboxTest {

    private static final @NotNull Logger LOGGER = LogManager.getLogger(InboxTest.class);

    @Autowired
    private @NotNull AuthController authController;

    @Autowired
    private @NotNull ProtectedController protectedController;

    @LocalServerPort
    private int port;

    @Autowired
    private @NotNull TestRestTemplate restTemplate;

    @Test
    void contextLoads() {
        assertThat(authController).isNotNull();
        assertThat(protectedController).isNotNull();
    }

    private static @NotNull Credentials credentials = new Credentials("test", "test");

    private static final @NotNull Gson gson = new GsonBuilder().create();

    @BeforeAll
    static void genCredentials() {
        credentials = new Credentials("test_" + UUID.randomUUID(), UUID.randomUUID().toString());
    }

    @Test
    void testInbox() {

        // register (/api/v0/auth/register)
        String registerUrl = "http://localhost:" + port + "/api/v0/auth/register";
        String registerBody = gson.toJson(credentials);

        String registerResponse = restTemplate.postForObject(registerUrl, registerBody, String.class);
        LOGGER.info("Register response: {}", registerResponse);
        assertNotNull(registerResponse);

    }

}
