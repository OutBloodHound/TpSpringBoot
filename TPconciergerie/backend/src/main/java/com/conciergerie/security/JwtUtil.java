package com.conciergerie.security;
// Paquetage qui regroupe toute la sécurité (JWT, filtre, config)

import com.conciergerie.entity.RoleUtilisateur;
// L'enum des rôles : ADMIN, CONCIERGE, PRESTATAIRE, CLIENT
// Servira à stocker le rôle dans le token JWT
import io.jsonwebtoken.Claims;
// Claims = le "corps" du token JWT (les données qu'il contient : subject, role, dates...)
import io.jsonwebtoken.Jwts;
// Classe principale de la librairie JJWT : permet de builder, parser, signer les tokens
import io.jsonwebtoken.security.Keys;
// Utilitaire pour créer une clé HMAC sécurisée à partir d'une chaîne
import org.springframework.beans.factory.annotation.Value;
// Injecte une valeur depuis application.yml (ex: ${application.security.jwt.secret-key})
import org.springframework.stereotype.Component;
// Annotation Spring : déclare cette classe comme un bean géré par le conteneur Spring
// Pourra être injecté (via @Autowired) dans d'autres classes

import javax.crypto.SecretKey;
// Type Java pour représenter une clé secrète (utilisée pour signer/valider les JWT)
import java.nio.charset.StandardCharsets;
//Fournit UTF_8 pour convertir la chaîne secrète en bytes de façon standardisée
import java.util.Date;
// Pour les dates d'émission (issuedAt) et d'expiration du token

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