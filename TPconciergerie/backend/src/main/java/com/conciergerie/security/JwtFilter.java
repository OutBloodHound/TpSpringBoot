package com.conciergerie.security;

import com.conciergerie.entity.Utilisateur;
import com.conciergerie.repository.UtilisateurRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
// Les 3 imports Servlet : représentent la requête HTTP entrante, la réponse sortante,
// et la chaîne de filtres (pour passer au filtre suivant)

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
// = l'objet qui représente "un utilisateur authentifié" aux yeux de Spring Security
// Contient : les infos de l'utilisateur + ses rôles/autorités
import org.springframework.security.core.context.SecurityContextHolder;
// = le coffre-fort de la requête en cours
// Spring Security y stocke l'utilisateur authentifié pour toute la durée de la requête
import org.springframework.security.core.authority.SimpleGrantedAuthority;
// = représente un "droit" ou "rôle" (ex: "ROLE_ADMIN")
// Simple car c'est juste une chaîne de caractères

import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
// = utilitaire qui ajoute des détails à l'authentification (IP, session...)
// Permet de construire proprement le UsernamePasswordAuthenticationToken
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
// OncePerRequestFilter = garantit que le filtre ne s'exécute qu'UNE SEULE fois par requête
// (même si la requête passe par plusieurs chaînes de filtres)

import java.io.IOException;
import java.util.List;
// Import pour les exceptions et les listes (coller les rôles)

@Component
// Bean Spring = injectable, détecté automatiquement
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter{
    // extends OncePerRequestFilter = Spring appelle doFilterInternal() pour chaque requête
    private final JwtUtil jwtUtil;
    private final UtilisateurRepository utilisateurRepository;
    // Injectés via le constructeur (JwtUtil pour parser le token, UtilisateurRepository pour
    // charger l'utilisateur depuis la DB et vérifier qu'il existe encore)

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
        throws ServletException, IOException {
        // 1. Récupérer le header "Authorization"
        String authHeader = request.getHeader("Authorization");
        // Format attendu : "Bearer eyJhbGciOiJIUzI1NiJ9..."
        // Si absent ou mal formaté → on passe au filtre suivant (Spring décidera 401)

        // 2. Vérifier que le header commence par "Bearer "
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        // 3. Extraire le token (enlever "Bearer ")
        String token = authHeader.substring(7);
        // "Bearer " fait 7 caractères → on prend tout après

        // 4. Extraire l'email depuis le token
        String email = jwtUtil.extractEmail(token);
        // JwtUtil.extractEmail() parse le token, vérifie la signature, et sort le subject

        // 5. Vérifier qu'on a un email ET qu'aucune authentification n'existe déjà
        if (email != null && SecurityContextHolder.getContext().getAuthentication() == null){
            // Sécurité : on ne remplace pas une authentification existante

            // 6. Charger l'utilisateur depuis la DB
            Utilisateur utilisateur = utilisateurRepository.findByEmail(email)
                    .orElse(null);
            // Vérifie que l'utilisateur existe encore (pas supprimé entre-temps)
            // Si pas trouvé → null → pas d'authentification

            // 7. Vérifier que le token est valide ET que l'utilisateur est actif
            if (utilisateur != null && jwtUtil.isTokenValid(token) && utilisateur.isActif()) {

                // 8. Créer la liste des autorités (rôles) pour Spring Security
                List<SimpleGrantedAuthority> authorities = List.of(
                        new SimpleGrantedAuthority("ROLE_" + utilisateur.getRole().name())
                );
                // SimpleGrantedAuthority = "ROLE_ADMIN", "ROLE_CLIENT"...
                // Spring Security exige le préfixe "ROLE_" pour hasRole()/hasAuthority()

                // 9. Créer l'objet d'authentification
                UsernamePasswordAuthenticationToken authToken =
                        new UsernamePasswordAuthenticationToken(
                                email,// principal = identifiant
                                null,// credentials = null (pas de mdp ici)
                                authorities// autorités = la liste des rôles
                        );

                // 10. Ajouter les détails de la requête (IP, session...)
                authToken.setDetails(
                        new WebAuthenticationDetailsSource().buildDetails(request)
                );

                // 11. Stocker l'authentification dans le SecurityContext
                SecurityContextHolder.getContext().setAuthentication(authToken);
                // À partir d'ici, Spring Security considère la requête AUTHENTIFIÉE
                // Dans les controllers, @AuthenticationPrincipal récupère l'email

            }
        }

        // 12. Passer au filtre suivant (quoi qu'il arrive)
        filterChain.doFilter(request, response);
    }
}