package com.conciergerie.dto;

import com.conciergerie.entity.TypeClient;
import lombok.Data;

@Data

public class ClientDTO {
    private Long id;
    private String adresse;
    private String batiment;
    private String etage;
    private TypeClient typeClient;
    private Long utilisateurId;
}
