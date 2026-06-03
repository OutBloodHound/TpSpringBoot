package com.conciergerie.dto;

import com.conciergerie.entity.TypeRessource;
import lombok.Data;

@Data

public class RessourceDTO {
    private Long id;
    private String nom;
    private TypeRessource typeRessource;
    private String description;
    private Integer capacite;
    private boolean actif = true;
}
