package net.juligames.goodproxy.web;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.responses.ApiResponse;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import net.juligames.goodproxy.util.ProfileUtil;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class OpenAPIConfiguration {

    @Bean
    public @NotNull OpenAPI customOpenAPI(ProfileUtil profileUtil) {
        OpenAPI goodProxyApi = new OpenAPI()
                .info(new Info()
                        .title("GoodProxy API")
                        .version("v0.0.1")
                        .description("API documentation for GoodProxy")
                        .contact(new Contact().email("bentzin@tdrstudios.de"))
                )
                .addSecurityItem(new SecurityRequirement().addList("bearerAuth"))
                .components(new Components()
                        .addSecuritySchemes("bearerAuth", new SecurityScheme()
                                .type(SecurityScheme.Type.HTTP)
                                .scheme("bearer")
                                .bearerFormat("JWT")
                        )
                        .responses(globalResponses()) // Add global responses properly
                );

        //check for prod profile
        if (profileUtil.isProfileActive("prod")) {
            goodProxyApi.addServersItem(new Server().url("https://banking.juligames.net").description("Production server"));
        } else {
            goodProxyApi.addServersItem(new Server().url("http://localhost:8080").description("Local development server"));
        }

        return goodProxyApi;
    }

    private @NotNull Map<String, ApiResponse> globalResponses() {
        Map<String, ApiResponse> responses = new HashMap<>();

        // 1xx Informational
        responses.put("100", new ApiResponse().description("Continue"));
        responses.put("101", new ApiResponse().description("Switching Protocols"));
        responses.put("102", new ApiResponse().description("Processing (WebDAV)"));

        // 2xx Success
        responses.put("200", new ApiResponse().description("OK"));
        responses.put("201", new ApiResponse().description("Created"));
        responses.put("202", new ApiResponse().description("Accepted"));
        responses.put("203", new ApiResponse().description("Non-Authoritative Information"));
        responses.put("204", new ApiResponse().description("No Content"));
        responses.put("205", new ApiResponse().description("Reset Content"));
        responses.put("206", new ApiResponse().description("Partial Content"));
        responses.put("207", new ApiResponse().description("Multi-Status (WebDAV)"));
        responses.put("208", new ApiResponse().description("Already Reported (WebDAV)"));
        responses.put("226", new ApiResponse().description("IM Used (HTTP Delta Encoding)"));

        // 3xx Redirection
        responses.put("300", new ApiResponse().description("Multiple Choices"));
        responses.put("301", new ApiResponse().description("Moved Permanently"));
        responses.put("302", new ApiResponse().description("Found"));
        responses.put("303", new ApiResponse().description("See Other"));
        responses.put("304", new ApiResponse().description("Not Modified"));
        responses.put("305", new ApiResponse().description("Use Proxy"));
        responses.put("307", new ApiResponse().description("Temporary Redirect"));
        responses.put("308", new ApiResponse().description("Permanent Redirect"));

        // 4xx Client Errors
        responses.put("400", new ApiResponse().description("Bad Request"));
        responses.put("401", new ApiResponse().description("Unauthorized"));
        responses.put("402", new ApiResponse().description("Payment Required"));
        responses.put("403", new ApiResponse().description("Forbidden"));
        responses.put("404", new ApiResponse().description("Not Found"));
        responses.put("405", new ApiResponse().description("Method Not Allowed"));
        responses.put("406", new ApiResponse().description("Not Acceptable"));
        responses.put("407", new ApiResponse().description("Proxy Authentication Required"));
        responses.put("408", new ApiResponse().description("Request Timeout"));
        responses.put("409", new ApiResponse().description("Conflict"));
        responses.put("410", new ApiResponse().description("Gone"));
        responses.put("411", new ApiResponse().description("Length Required"));
        responses.put("412", new ApiResponse().description("Precondition Failed"));
        responses.put("413", new ApiResponse().description("Payload Too Large"));
        responses.put("414", new ApiResponse().description("URI Too Long"));
        responses.put("415", new ApiResponse().description("Unsupported Media Type"));
        responses.put("416", new ApiResponse().description("Range Not Satisfiable"));
        responses.put("417", new ApiResponse().description("Expectation Failed"));
        responses.put("418", new ApiResponse().description("I'm a teapot"));
        responses.put("421", new ApiResponse().description("Misdirected Request"));
        responses.put("422", new ApiResponse().description("Unprocessable Entity (WebDAV)"));
        responses.put("423", new ApiResponse().description("Locked (WebDAV)"));
        responses.put("424", new ApiResponse().description("Failed Dependency (WebDAV)"));
        responses.put("425", new ApiResponse().description("Too Early"));
        responses.put("426", new ApiResponse().description("Upgrade Required"));
        responses.put("428", new ApiResponse().description("Precondition Required"));
        responses.put("429", new ApiResponse().description("Too Many Requests"));
        responses.put("431", new ApiResponse().description("Request Header Fields Too Large"));
        responses.put("451", new ApiResponse().description("Unavailable For Legal Reasons"));

        // 5xx Server Errors
        responses.put("500", new ApiResponse().description("Internal Server Error"));
        responses.put("501", new ApiResponse().description("Not Implemented"));
        responses.put("502", new ApiResponse().description("Bad Gateway"));
        responses.put("503", new ApiResponse().description("Service Unavailable"));
        responses.put("504", new ApiResponse().description("Gateway Timeout"));
        responses.put("505", new ApiResponse().description("HTTP Version Not Supported"));
        responses.put("506", new ApiResponse().description("Variant Also Negotiates"));
        responses.put("507", new ApiResponse().description("Insufficient Storage (WebDAV)"));
        responses.put("508", new ApiResponse().description("Loop Detected (WebDAV)"));
        responses.put("510", new ApiResponse().description("Not Extended"));
        responses.put("511", new ApiResponse().description("Network Authentication Required"));

        return responses;
    }
}
