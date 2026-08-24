package com.wanaia.domain.user.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.UUID;

@Component
public class JwtTokenProvider {

    private static final Logger log = LoggerFactory.getLogger(JwtTokenProvider.class);

    private final SecretKey key;
    private final long accessTokenExpirationMs;

    public JwtTokenProvider(
        @Value("${wanaia.jwt.secret:WanaiaSecretKeyForJwtAuthenticationMustBeAtLeast256BitsLongForHmacSha256SecurityRequirement}") String secret,
        @Value("${wanaia.jwt.access-expiration-ms:900000}") long accessTokenExpirationMs // 15 minutes default
    ) {
        this.key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
        this.accessTokenExpirationMs = accessTokenExpirationMs;
    }

    public String generateAccessToken(UserPrincipal userPrincipal) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + accessTokenExpirationMs);

        return Jwts.builder()
            .subject(userPrincipal.getId().toString())
            .claim("email", userPrincipal.getUsername())
            .claim("role", userPrincipal.getRole().name())
            .claim("uuid", userPrincipal.getUuid().toString())
            .issuedAt(now)
            .expiration(expiryDate)
            .issuer("wanaia-api")
            .signWith(key)
            .compact();
    }

    public Long getUserIdFromJwt(String token) {
        Claims claims = Jwts.parser()
            .verifyWith(key)
            .build()
            .parseSignedClaims(token)
            .getPayload();

        return Long.parseLong(claims.getSubject());
    }

    public boolean validateToken(String authToken) {
        try {
            Jwts.parser().verifyWith(key).build().parseSignedClaims(authToken);
            return true;
        } catch (JwtException | IllegalArgumentException ex) {
            log.warn("Invalid JWT token: {}", ex.getMessage());
            return false;
        }
    }

    public long getAccessTokenExpirationMs() {
        return accessTokenExpirationMs;
    }
}
