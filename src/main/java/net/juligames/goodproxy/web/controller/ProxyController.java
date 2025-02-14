package net.juligames.goodproxy.web.controller;

import net.juligames.goodproxy.web.Echo;
import net.juligames.goodproxy.web.WebResponse;
import org.jetbrains.annotations.NotNull;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController("/api/v0/proxy")
public class ProxyController {

    @GetMapping("/error")
    public @NotNull WebResponse<String> testError() {
        return WebResponse.from(() -> {
            throw new RuntimeException("This is a test error");
        });
    }
}
