package net.juligames.goodproxy.web.controller;

import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Controller
public class IndexController {

    @GetMapping("/")
    public @NotNull String index() {
        return "index";
    }

    @GetMapping("/v0")
    public @NotNull String v0() {
        return "v0";
    }
}
