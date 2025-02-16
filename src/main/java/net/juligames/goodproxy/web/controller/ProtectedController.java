package net.juligames.goodproxy.web.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import net.juligames.goodproxy.displaymessage.DisplayMessage;
import net.juligames.goodproxy.prx.ProxyAPI;
import net.juligames.goodproxy.web.filter.JwtFilter;
import net.juligames.goodproxy.web.schema.InboxIndex;
import net.juligames.goodproxy.web.schema.PayResult;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.atomic.AtomicInteger;

@RestController
@RequestMapping("/api/v0/protected")
@SecurityRequirement(name = "bearerAuth")
public class ProtectedController {

    private final @NotNull Logger logger = LogManager.getLogger(ProtectedController.class);
    private final @NotNull JwtFilter jwtFilter;

    public ProtectedController(@NotNull JwtFilter jwtFilter) {
        this.jwtFilter = jwtFilter;
    }

    @Operation(summary = "Get a hello response",
            description = "Returns a boolean value of true")
    @GetMapping("/hello")
    public boolean hello() {
        return true;
    }

    @Operation(summary = "Get the balance of the user",
            description = "Returns the balance of the user")
    @GetMapping("/balance")
    public double balance(@Parameter(hidden = true) @RequestHeader("Authorization") @NotNull String token) throws ExecutionException, InterruptedException {
        ProxyAPI proxyAPI = getProxyAPIFromHeader(token);
        return proxyAPI.balance().get().getPayload();
    }

    @Operation(summary = "Pay another user a specified amount",
            description = "Pays another user the specified amount")
    @GetMapping("/pay")
    public @NotNull PayResult pay(@Parameter(hidden = true) @RequestHeader("Authorization") @NotNull String token,
                                  @RequestParam @NotNull String receiver,
                                  @RequestParam int amount) throws ExecutionException, InterruptedException {
        ProxyAPI proxyAPI = getProxyAPIFromHeader(token);
        DisplayMessage displayMessage = proxyAPI.pay(receiver, amount).get();
        boolean success = "pay.success".equals(displayMessage.key());
        return new PayResult(receiver, amount, success, displayMessage.key(), displayMessage.message());
    }

    @Operation(summary = "Get your inbox")
    @GetMapping("/inbox")
    public @NotNull List<InboxIndex> inbox(@Parameter(hidden = true) @RequestHeader("Authorization") @NotNull String token, boolean privateMessages) throws ExecutionException, InterruptedException {
        ProxyAPI proxyAPI = getProxyAPIFromHeader(token);
        List<String> strings = proxyAPI.getInboxAll().get();
        logger.debug("Got {} messages from the inbox", strings.size());
        AtomicInteger id = new AtomicInteger(1);
        return strings.stream().map(s -> new InboxIndex(id.getAndIncrement(), privateMessages)).toList();
    }


    /**
     * Retrieves the ProxyAPI from the active session using the Authorization header.
     *
     * @param authorizationHeader The Authorization header containing the JWT.
     * @return The ProxyAPI object associated with the session.
     */
    @SuppressWarnings("ConstantValue")
    protected @NotNull ProxyAPI getProxyAPIFromHeader(@NotNull String authorizationHeader) {
        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            throw new IllegalStateException("Missing or invalid Authorization header.");
        }
        String jwt = authorizationHeader.replace("Bearer ", "");

        ProxyAPI proxyAPI = JwtFilter.getSession(jwt);
        if (proxyAPI == null) {
            throw new IllegalStateException("Session not found for the provided token.");
        }
        return proxyAPI;
    }
}
