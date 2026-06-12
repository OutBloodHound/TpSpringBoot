package com.conciergerie.config;
// Dans le package config (avec CorsConfig), pas dans security

import com.conciergerie.security.JwtFilter;
// Le filtre qu'on vient d'écrire — on va le brancher ici

import lombok.RequiredArgsConstructor;
// Constructeur automatique pour l'injection du JwtFilter

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
// = "Cette classe contient des définitions de beans Spring
@EnableWebSecurity
// = "Active la configuration Web de Spring Security"
// Sans ça, les SecurityFilterChain sont ignorés

@RequiredArgsConstructor
// Lombok génère le constructeur pour final JwtFilter jwtFilter

public class SecurityConfig {
    private final JwtFilter jwtFilter;
    // Injection du filtre qu'on a écrit — on le branchera dans le SecurityFilterChain

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception{
        // C'est le coeur de la config : on reçoit l'objet HttpSecurity de Spring
        // et on le configure avec nos règles
      http
              .csrf(csrf -> csrf.disable())
              // CSRF = protection contre les attaques cross-site request forgery
              // On désactive car on utilise des tokens JWT (stateless)
              // Les tokens JWT ne sont pas vulnérables au CSRF
              // (ils sont dans le header Authorization, pas dans un cookie)

              .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                // CORS = Cross-Origin Resource Sharing
                // On branche notre configuration CORS personnalisée
                // Sans ça, le navigateur bloquerait les requêtes depuis le frontend React
              .sessionManagement(Session->
                      session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                // On dit à Spring : "ne crée PAS de session HTTP"
                // Chaque requête est indépendante
                // L'utilisateur est identifié par son token JWT à chaque requête
                // STATELESS = pas de HttpSession, pas de cookie JSESSIONID
              .authorizeHttpRequests(auth -> auth
                      // On définit QUI peut accéder à QUOI
                      // L'ordre est crucial : Spring prend la PREMIÈRE règle qui match
                      .requestMatchers("/api/auth/**").permitAll()
                      //api/auth/* = login, register, refresh
                      // ACCESSIBLE À TOUS — pas besoin de token pour se connecter
                      .requestMatchers("/api-docs/**", "/swagger-ui/**", "/swagger-ui.html").permitAll()
                      .requestMatchers("/api/admin/**").hasRole("ADMIN")
                      .requestMatchers("/api/concierge/**").hasAnyRole("ADMIN", "CONCIERGE")
                      .anyRequest().authenticated()
              )

              .httpBasic(httpBasic -> httpBasic.disable())
              .formLogin(formLogin -> formLogin.disable())
              .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);
      return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedOrigins(List.of("http://localhost:3000"));
        // Frontend React en dev sur le port 3000
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS"));
        config.setAllowedHeaders(List.of("*"));
        config.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }

}