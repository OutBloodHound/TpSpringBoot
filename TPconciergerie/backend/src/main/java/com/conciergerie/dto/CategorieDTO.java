package com.conciergerie.dto;

import lombok.Data;

@Data

public class CategorieDTO {
    private Long id;
    private String libelle;
    private String description;
    private String icone;
    private String couleur;
    private Integer delaiSlaHeures;
}
