package com.conciergerie.dto;

import lombok.Data;

@Data

public class PrestataireDTO {
    private Long id;
    private String zoneGeo;
    private float noteMoyenne;
    private int nbInterventions;
    private boolean disponible = true;
}
