package net.juligames.goodproxy.config;

import net.juligames.goodproxy.web.filter.JwtFilter;
import org.jetbrains.annotations.NotNull;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class SecurityConfig {

    private final @NotNull JwtFilter jwtFilter;

    public SecurityConfig(@NotNull JwtFilter jwtFilter) {
        this.jwtFilter = jwtFilter;
    }

    @Bean
    public @NotNull SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(csrf -> csrf.disable()) // Disable CSRF for REST APIs
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/v0/protected/**").authenticated() // Secure only /api/v0/protected/*
                        .anyRequest().permitAll() // Allow other endpoints
                )
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class) // Add custom JWT filter
                .sessionManagement(session -> session.disable()) // Stateless
                .formLogin(form -> form.disable()); // Disable login form
        return http.build();
    }

    @Bean
    public @NotNull PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public @NotNull AuthenticationManager authenticationManager() {
        // No UserDetailsService because we use ProxyAPI for authentication
        return authentication -> {
            throw new UnsupportedOperationException("Custom JWT filter handles authentication");
        };
    }
}
