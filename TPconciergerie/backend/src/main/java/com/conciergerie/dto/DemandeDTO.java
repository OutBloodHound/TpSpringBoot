package com.conciergerie.dto;

import com.conciergerie.entity.Priorite;
import com.conciergerie.entity.Statut;
import lombok.Data;

import java.time.LocalDateTime;

@Data

public class DemandeDTO {
    private Long id;
    private String titre;
    private String description;
    private Statut statut;
    private Priorite priorite;
    private LocalDateTime creeLe;
    private LocalDateTime clotureLe;
    private Long clientId;
    private Long prestataireId;
    private Long categorieId;
}
