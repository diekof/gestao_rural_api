package com.seuprojeto.agro.security;

import com.seuprojeto.agro.common.Role;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.UUID;
import javax.crypto.SecretKey;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class JwtService {

    private final SecretKey key;
    private final long accessExpMinutes;
    private final long refreshExpDays;

    public JwtService(@Value("${jwt.secret}") String secret,
                      @Value("${jwt.access-token-expiration-minutes}") long accessExpMinutes,
                      @Value("${jwt.refresh-token-expiration-days}") long refreshExpDays) {
        this.key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
        this.accessExpMinutes = accessExpMinutes;
        this.refreshExpDays = refreshExpDays;
    }

    public String generateAccessToken(AuthenticatedUser user) {
        Instant now = Instant.now();
        return Jwts.builder()
                .subject(user.userId().toString())
                .claim("username", user.username())
                .claim("tenantId", user.tenantId() == null ? null : user.tenantId().toString())
                .claim("role", user.role().name())
                .issuedAt(Date.from(now))
                .expiration(Date.from(now.plus(accessExpMinutes, ChronoUnit.MINUTES)))
                .signWith(key)
                .compact();
    }

    public String generateRefreshToken(AuthenticatedUser user) {
        Instant now = Instant.now();
        return Jwts.builder()
                .subject(user.userId().toString())
                .claim("type", "refresh")
                .issuedAt(Date.from(now))
                .expiration(Date.from(now.plus(refreshExpDays, ChronoUnit.DAYS)))
                .signWith(key)
                .compact();
    }

    public Claims parse(String token) {
        return Jwts.parser().verifyWith(key).build().parseSignedClaims(token).getPayload();
    }

    public AuthenticatedUser toUser(Claims claims) {
        String tenant = claims.get("tenantId", String.class);
        return new AuthenticatedUser(
                UUID.fromString(claims.getSubject()),
                tenant == null ? null : UUID.fromString(tenant),
                claims.get("username", String.class),
                Role.valueOf(claims.get("role", String.class))
        );
    }
}
