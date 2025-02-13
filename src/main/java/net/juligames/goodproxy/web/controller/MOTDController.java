package net.juligames.goodproxy.web.controller;

import net.juligames.goodproxy.prx.ProxyAPI;
import net.juligames.goodproxy.prx.ProxyAPIFactory;
import net.juligames.goodproxy.web.MOTD;
import net.juligames.goodproxy.web.WebResponse;
import org.jetbrains.annotations.NotNull;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.ExecutionException;

@RestController
public class MOTDController {

    private static final @NotNull ProxyAPI API = ProxyAPIFactory.create();

    @GetMapping("/v0/motd")
    public @NotNull WebResponse<MOTD> motd() throws ExecutionException, InterruptedException {
        return new WebResponse<>(0, System.currentTimeMillis(), new MOTD(API.motd().get()));
    }
}
