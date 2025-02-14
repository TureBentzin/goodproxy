package net.juligames.goodproxy.web.controller;

import jakarta.validation.constraints.Null;
import net.juligames.goodproxy.prx.ProxyAPI;
import net.juligames.goodproxy.prx.ProxyAPIFactory;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.ExecutionException;

/**
 * This Controller manages public endpoints that are routed to the BankAPI.
 * These don't require authentication.
 */
@RestController
@RequestMapping("/api/v0/public")
public class PublicController {

    private static final @NotNull ProxyAPI API = ProxyAPIFactory.create();

    /**
     * Get the message of the day
     */
    @GetMapping("/motd")
    public @NotNull String motd() throws ExecutionException, InterruptedException {
        return API.motd().get();
    }

    @RequestMapping(path = "/echo", method = {RequestMethod.POST, RequestMethod.GET})
    public @NotNull String echo(
            @RequestParam(value = "message", required = false) @NotNull Optional<String> message
    ) throws Exception {
        @Nullable String result = null;
        if (message.isPresent()) {
            result = API.echo(message.get()).get().getValue1();
        } else {
            result = API.echo().get().getValue1();
        }
        return Objects.requireNonNull(result, "Result is null");
    }


}
