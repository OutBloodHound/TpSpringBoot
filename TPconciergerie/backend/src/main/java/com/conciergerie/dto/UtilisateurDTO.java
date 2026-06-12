package com.conciergerie.dto;
// Paquetage des DTOs (Data Transfer Objects)
// Un DTO = une version "légère" de l'entité, utilisée pour les échanges avec le frontend

import com.conciergerie.entity.RoleUtilisateur;
// Import de l'enum pour typer le champ role (même enum que l'entité)

import lombok.Data;
// @Data = raccourci Lombok qui combine :
//   @Getter + @Setter + @ToString + @EqualsAndHashCode + @RequiredArgsConstructor
// Génère TOUT automatiquement sans écrire une ligne de code

@Data
// Lombok va générer : constructeur vide, getters, setters, toString, equals, hashCode

public class UtilisateurDTO {
    // DTO = ce que le frontend reçoit et envoie
    // Contrairement à l'entité, on NE met PAS :
    //   - le mot de passe (sécurité : on ne renvoie JAMAIS le hash)
    //   - la date de création (gérée automatiquement)
    //   - les relations @OneToMany (clients, commentaires, etc.)

    private Long id;
    // null pour une création, rempli pour une lecture/modification

    private String nom;
    private String prenom;
    private String email;
    private RoleUtilisateur role;
    // Même enum que l'entité — le frontend reçoit "ADMIN", "CLIENT"...

    private String telephone;
    private boolean actif;
    // isActif() pour le getter (Lombok suit la convention JavaBean)
}