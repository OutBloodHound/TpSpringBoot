package com.conciergerie.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data

public class CommentaireDTO {
    private Long id;
    private String texte;
    private boolean interne;
    private LocalDateTime creeLe;
    private Long demandeId;
    private Long utilisateurId;
}
