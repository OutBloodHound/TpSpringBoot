package com.conciergerie.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data

public class PieceJointeDTO {
    private Long id;
    private String nomFichier;
    private String chemin;
    private String mimeType;
    private int tailleOctets;
    private LocalDateTime creeLe;
    private Long demandeId;
}
