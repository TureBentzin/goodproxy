package net.juligames.goodproxy.web.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.jetbrains.annotations.NotNull;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v0/protected")
@SecurityRequirement(name = "bearerAuth")
public class ProtectedController {

    @Operation(summary = "Checks if you are authenticated")
    @GetMapping("/hello")
    public boolean hello() {
        return true;
    }
}
