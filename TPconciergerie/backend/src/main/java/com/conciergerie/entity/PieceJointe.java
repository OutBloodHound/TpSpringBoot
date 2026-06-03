package com.conciergerie.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Table(name = "piece_jointe")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class PieceJointe implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "demande_id", nullable = false)
    private Demande demande;

    @Column(name = "nom_fichier", nullable = false, length = 255)
    private String nomFichier;

    @Column(nullable = false, length = 500)
    private String chemin;

    @Column(name = "mime_type", nullable = false, length = 100)
    private String mimeType;

    @Column(name = "taille_octets", nullable = false)
    private int tailleOctets;

    @Column(name = "cree_le", nullable = false, updatable = false)
    private LocalDateTime creeLe;
}
