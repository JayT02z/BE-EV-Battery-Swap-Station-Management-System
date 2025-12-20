package com.boilerplate.auth.security.jwt;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Component
@Slf4j
public class JwtTokenProvider {

    @Value("${jwt.secret}")
    private String jwtSecret;

    @Value("${jwt.access-token-expiration:3600000}")
    private long accessTokenExpiration;

    @Value("${jwt.refresh-token-expiration:86400000}")
    private long refreshTokenExpiration;

    private SecretKey key;

    @PostConstruct
    public void init() {
        this.key = Keys.hmacShaKeyFor(jwtSecret.getBytes(StandardCharsets.UTF_8));
        log.info("JWT Token Provider initialized");
    }

    /* =====================================================
       USER TOKEN (giữ nguyên)
       ===================================================== */

    public String generateAccessToken(Authentication authentication) {
        String username = authentication.getName();
        String roles = authentication.getAuthorities().stream()
            .map(GrantedAuthority::getAuthority)
            .collect(Collectors.joining(","));

        return buildToken(username, roles, null, accessTokenExpiration);
    }

    public String generateAccessToken(String username, String role) {
        return buildToken(username, role, null, accessTokenExpiration);
    }

    public String generateRefreshToken(String username) {
        return buildToken(username, null, null, refreshTokenExpiration);
    }

    public String generateAccessTokenWithUserInfo(
        String username,
        String role,
        String fullName,
        String email
    ) {
        return Jwts.builder()
            .subject(username)
            .claim("role", role)
            .claim("fullName", fullName)
            .claim("email", email)
            .issuedAt(new Date())
            .expiration(new Date(System.currentTimeMillis() + accessTokenExpiration))
            .signWith(key)
            .compact();
    }

    /* =====================================================
       INTERNAL SERVICE TOKEN (MỚI)
       ===================================================== */

    public String generateInternalServiceToken(String serviceName) {
        return Jwts.builder()
            .subject(serviceName)
            .claim("type", "INTERNAL")
            .claim("roles", List.of("SERVICE"))
            .issuedAt(new Date())
            .expiration(new Date(System.currentTimeMillis() + 60_000)) // 60s
            .signWith(key)
            .compact();
    }

    /* =====================================================
       COMMON
       ===================================================== */

    private String buildToken(
        String subject,
        String role,
        Claims extraClaims,
        long expirationMs
    ) {
        Date now = new Date();
        Date expiry = new Date(now.getTime() + expirationMs);

        JwtBuilder builder = Jwts.builder()
            .subject(subject)
            .issuedAt(now)
            .expiration(expiry);

        if (role != null) {
            builder.claim("role", role);
        }

        if (extraClaims != null) {
            builder.addClaims(extraClaims);
        }

        return builder.signWith(key).compact();
    }

    public boolean validateToken(String token) {
        try {
            parseClaims(token);
            return true;
        } catch (JwtException | IllegalArgumentException ex) {
            log.error("JWT validation failed: {}", ex.getMessage());
            return false;
        }
    }

    public Claims getAllClaims(String token) {
        return parseClaims(token);
    }

    public String getUsernameFromToken(String token) {
        return parseClaims(token).getSubject();
    }

    public String getRolesFromToken(String token) {
        return parseClaims(token).get("role", String.class);
    }

    private Claims parseClaims(String token) {
        return Jwts.parser()
            .verifyWith(key)
            .build()
            .parseSignedClaims(token)
            .getPayload();
    }
}
