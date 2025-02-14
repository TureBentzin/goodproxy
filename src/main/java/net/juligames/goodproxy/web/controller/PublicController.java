package net.juligames.goodproxy.web.controller;

import net.juligames.goodproxy.prx.ProxyAPI;
import net.juligames.goodproxy.prx.ProxyAPIFactory;
import net.juligames.goodproxy.web.Echo;
import net.juligames.goodproxy.web.MOTD;
import net.juligames.goodproxy.web.WebResponse;
import org.jetbrains.annotations.NotNull;
import org.springframework.web.bind.annotation.*;

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
    public @NotNull WebResponse<MOTD> motd() {
        return WebResponse.from(() -> new MOTD(API.motd().get()));
    }

    @RequestMapping(path = "/echo", method = {RequestMethod.POST, RequestMethod.GET})
    public @NotNull WebResponse<Echo> echo(
            @RequestParam(value = "message", required = false) @NotNull Optional<String> message
    ) {
        return WebResponse.from(() -> {
            if (message.isEmpty()) {
                return new Echo(API.echo().get().getValue1());
            } else return new Echo(API.echo(message.get()).get().getValue1());
        });
    }


}
