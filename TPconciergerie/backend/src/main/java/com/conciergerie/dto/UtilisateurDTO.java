package com.conciergerie.dto;

import com.conciergerie.entity.RoleUtilisateur;
import lombok.Data;

@Data

public class UtilisateurDTO {
    private Long id;
    private String nom;
    private String prenom;
    private String email;
    private RoleUtilisateur role;
    private String telephone;
    private boolean actif;
}