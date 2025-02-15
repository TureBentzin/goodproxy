package net.juligames.goodproxy.web.filter;

import net.juligames.goodproxy.prx.ProxyAPI;
import net.juligames.goodproxy.util.JwtUtil;
import org.jetbrains.annotations.NotNull;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.Collections;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class JwtFilter extends OncePerRequestFilter {

    private final @NotNull JwtUtil jwtUtil;
    private static final @NotNull ConcurrentHashMap<String, ProxyAPI> activeSessions = new ConcurrentHashMap<>();

    public JwtFilter(@NotNull JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Override
    protected void doFilterInternal(@NotNull HttpServletRequest request,
                                    @NotNull HttpServletResponse response,
                                    @NotNull FilterChain chain) throws ServletException, IOException {

        // Get Authorization header
        final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            chain.doFilter(request, response);
            return;
        }

        // Extract JWT
        final String jwt = authHeader.substring(7);

        // Validate the token and extract username
        if (jwtUtil.validateToken(jwt)) {
            String username = jwtUtil.extractUsername(jwt);

            // Retrieve ProxyAPI session for the token
            ProxyAPI proxyAPI = activeSessions.get(jwt);
            if (proxyAPI != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                UserDetails userDetails = new User(username, "", Collections.emptyList());

                UsernamePasswordAuthenticationToken authToken =
                        new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                // Set authentication in the security context
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }

        chain.doFilter(request, response);
    }

    /**
     * Store a ProxyAPI session against the JWT.
     */
    public static void storeSession(@NotNull String jwt, @NotNull ProxyAPI proxyAPI) {
        activeSessions.put(jwt, proxyAPI);
    }

    /**
     * Remove a ProxyAPI session for a JWT.
     */
    public static void removeSession(@NotNull String jwt) {
        activeSessions.remove(jwt).attemptClose();
    }

    public static @NotNull ConcurrentHashMap<String, ProxyAPI> getActiveSessions() {
        return activeSessions;
    }


    public static @NotNull ProxyAPI getSession(@NotNull String jwt) {
        return activeSessions.get(jwt);
    }

}
