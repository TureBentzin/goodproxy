package net.juligames.goodproxy.web.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import net.juligames.goodproxy.prx.ProxyAPI;
import net.juligames.goodproxy.web.filter.JwtFilter;
import org.jetbrains.annotations.NotNull;
import org.springframework.web.bind.annotation.*;

import javax.security.auth.login.AccountNotFoundException;
import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping("/api/v0/protected")
@SecurityRequirement(name = "bearerAuth")
public class ProtectedController {

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
