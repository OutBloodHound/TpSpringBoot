package com.conciergerie.dto;

import com.conciergerie.entity.StatutReservation;
import lombok.Data;

import java.time.LocalDateTime;

@Data

public class ReservationDTO {
    private Long id;
    private LocalDateTime dateDebut;
    private LocalDateTime dateFin;
    private StatutReservation statut;
    private String notes;
    private Long clientId;
    private Long ressourceId;
}
