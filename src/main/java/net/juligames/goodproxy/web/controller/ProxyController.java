package net.juligames.goodproxy.web.controller;

import io.swagger.v3.oas.annotations.Operation;
import net.juligames.goodproxy.web.error.RESTError;
import net.juligames.goodproxy.web.error.RESTException;
import net.juligames.goodproxy.web.error.RESTExceptionHandler;
import org.jetbrains.annotations.NotNull;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.TimeoutException;

@RestController
@RequestMapping("/api/v0/proxy")
public class ProxyController {

    @Operation(summary = "Get an example error response")
    @GetMapping("/error")
    public RESTExceptionHandler.@NotNull RESTErrorResponse error() {
        return RESTExceptionHandler.RESTErrorResponse.of(new Exception("exception text"), RESTError.EXAMPLE);
    }
}
