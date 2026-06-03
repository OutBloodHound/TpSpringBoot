package com.conciergerie.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data

public class NotationDTO {
    private Long id;
    private byte note;
    private String commentaire;
    private LocalDateTime creeLe;
    private Long demandeId;
    private Long prestataireId;
}
