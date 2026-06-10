package com.conciergerie.security;

import com.conciergerie.entity.RoleUtilisateur;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Component
public class JwtUtil {
    private final SecretKey signingKey;
    private final long expirationMs;
    private final long refreshExpirationMs;

    public JwtUtil(
            @Value("${application.security.jwt.secret-key}") String secretKey,
            @Value("${application.security.jwt.expiration-ms}") long expirationMs,
            @Value("${application.security.jwt.refresh-token-expiration-ms}") long refreshExpirationMs
    ) {
        this.signingKey = Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));
        this.expirationMs = expirationMs;
        this.refreshExpirationMs = refreshExpirationMs;
    }

    public String generateToken(String email, RoleUtilisateur role){
        return Jwts.builder()
                .subject(email)
                .claim("role", role.name())
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + expirationMs))
                .signWith(signingKey)
                .compact();
    }

    public String generateRefreshToken(String email){
        return Jwts.builder()
                .subject(email)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + refreshExpirationMs))
                .signWith(signingKey)
                .compact();
    }

    public String extractEmail(String token){
        return extractClaims(token).getSubject();
    }

    public String extractRole(String token){
        return extractClaims(token).get("role", String.class);
    }

    public boolean isTokenValid(String  token){
        try{
            extractClaims(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    private Claims extractClaims(String token){
        return Jwts.parser()
                .verifyWith(signingKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }


}