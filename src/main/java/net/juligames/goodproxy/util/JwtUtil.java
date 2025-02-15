package net.juligames.goodproxy.util;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.function.Function;

@Component
public class JwtUtil {

    private final @NotNull SecretKey secretKey = Keys.secretKeyFor(SignatureAlgorithm.HS256);

    @SuppressWarnings("FieldCanBeLocal")
    private final long keepAlive = 3600000; // 1 hour

    /**
     * Generates a JWT with username and password as claims.
     *
     * @param username the username
     * @param password the password (required for legacy backend)
     * @return signed JWT
     */
    public @NotNull String generateToken(@NotNull String username, @NotNull String password) {
        return Jwts.builder()
                .setSubject(username)
                .claim("password", password) // Store password as a custom claim
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + keepAlive))
                .signWith(secretKey)
                .compact();
    }

    /**
     * Validates the JWT.
     *
     * @param token JWT token
     * @return true if valid, false otherwise
     */
    public boolean validateToken(@NotNull String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(secretKey)
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (JwtException e) {
            return false;
        }
    }

    /**
     * Extracts the username from the JWT.
     *
     * @param token JWT token
     * @return username
     */
    public @NotNull String extractUsername(@NotNull String token) {
        return extractClaim(token, Claims::getSubject);
    }

    /**
     * Extracts the password from the JWT.
     *
     * @param token JWT token
     * @return password claim
     */
    public @NotNull String extractPassword(@NotNull String token) {
        return extractClaim(token, claims -> claims.get("password", String.class));
    }

    /**
     * Extracts the expiration date from the JWT.
     *
     * @param token JWT token
     * @return expiration date
     */
    public @NotNull Date extractExpiration(@NotNull String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    /**
     * Extracts a custom claim from the JWT.
     *
     * @param token JWT token
     * @param claimsResolver claim resolver function
     * @param <T> type of claim
     * @return extracted claim
     */
    public <T> @NotNull T extractClaim(@NotNull String token, @NotNull Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    /**
     * Parses and extracts all claims from the JWT.
     *
     * @param token JWT token
     * @return all claims
     */
    private @NotNull Claims extractAllClaims(@NotNull String token) {
        return Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    /**
     * Checks if the token has expired.
     *
     * @param token JWT token
     * @return true if expired, false otherwise
     */
    private boolean isTokenExpired(@NotNull String token) {
        return extractExpiration(token).before(new Date());
    }
}
