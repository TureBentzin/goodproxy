package net.juligames.goodproxy.web.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import net.juligames.goodproxy.displaymessage.DisplayMessage;
import net.juligames.goodproxy.prx.ProxyAPI;
import net.juligames.goodproxy.prx.ProxyAPIFactory;
import net.juligames.goodproxy.util.Credentials;
import net.juligames.goodproxy.util.JwtUtil;
import net.juligames.goodproxy.web.error.RESTError;
import net.juligames.goodproxy.web.error.RESTException;
import net.juligames.goodproxy.web.filter.JwtFilter;
import net.juligames.goodproxy.web.schema.Authentication;
import net.juligames.goodproxy.web.schema.ProxyApiInfo;
import org.jetbrains.annotations.NotNull;
import org.springframework.web.bind.annotation.*;

import javax.security.sasl.AuthenticationException;
import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping("/api/v0/auth")
public class AuthController {

    private final @NotNull JwtUtil jwtUtil;

    public AuthController(@NotNull JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Operation(
            summary = "Register a new user",
            description = "Registers a new user with the provided credentials.",
            requestBody = @RequestBody(
                    description = "Username and password in JSON format",
                    required = true,
                    content = @Content(schema = @Schema(implementation = Credentials.class))
            ),
            responses = {
                    @ApiResponse(responseCode = "200", description = "Registration successful", content = @Content(schema = @Schema(implementation = Authentication.class)),
                            headers = {@io.swagger.v3.oas.annotations.headers.Header(name = "Authorization", description = "Bearer token", required = true)})
            }
    )
    @PostMapping("/register")
    public @NotNull Authentication register(@NotNull Credentials credentials) throws ExecutionException, InterruptedException {
        String jwt = jwtUtil.generateToken(credentials);
        ProxyAPI proxyAPI = ProxyAPIFactory.create();

        DisplayMessage message = proxyAPI.register(credentials).get();
        if (!"register.success".equals(message.key())) {
            proxyAPI.attemptClose();
            throw new RuntimeException("Registration failed");
        }

        // Store session in the JwtFilter
        JwtFilter.storeSession(jwt, proxyAPI);
        return new Authentication(jwt, message.message());
    }

    @Operation(
            summary = "Authenticate and obtain JWT token",
            description = "Returns a JWT token if the provided credentials are valid.",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Username and password in JSON format",
                    required = true,
                    content = @Content(schema = @Schema(implementation = Credentials.class))
            ),
            responses = {
                    @ApiResponse(responseCode = "200", description = "Authentication successful", content = @Content(schema = @Schema(implementation = Authentication.class)),
                            headers = {@io.swagger.v3.oas.annotations.headers.Header(name = "Authorization", description = "Bearer token", required = true)})
            }
    )
    @PostMapping("/authenticate")
    public @NotNull Authentication authenticate(@NotNull Credentials credentials) throws ExecutionException, InterruptedException {
        String jwt = jwtUtil.generateToken(credentials);
        ProxyAPI proxyAPI = ProxyAPIFactory.create();

        DisplayMessage message = proxyAPI.authenticate(credentials).get();
        if (!"auth.success".equals(message.key())) {
            proxyAPI.attemptClose();
            throw new RESTException(RESTError.INVALID_CREDS, message.message());
        }

        // Store session in the JwtFilter
        JwtFilter.storeSession(jwt, proxyAPI);
        return new Authentication(jwt, message.message());
    }

    @SecurityRequirement(name = "bearerAuth")
    @PostMapping("/logout")
    public void logout(@RequestHeader("Authorization") String token) {
        String jwt = token.substring(7);
        JwtFilter.removeSession(jwt);
    }

    @GetMapping("/proxy-info")
    public @NotNull ProxyApiInfo getProxyInfo(@RequestHeader("Authorization") String token) {
        String jwt = token.substring(7);
        return new ProxyApiInfo(jwtUtil.extractUsername(jwt));
    }
}
