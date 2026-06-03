package com.conciergerie.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "demande")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class Demande implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 255)
    private String titre;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 10)
    private Statut statut;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 7)
    private Priorite priorite;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "client_id", nullable = false)
    private Client client;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "prestataire_id")
    private Prestataire prestataire;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "categorie_id", nullable = false)
    private Categorie categorie;

    @Column(name = "cree_le", nullable = false, updatable = false)
    private LocalDateTime creeLe;

    @Column(name = "cloture_le")
    private LocalDateTime clotureLe;

    @OneToMany(mappedBy = "demande")
    private Set<Commentaire> commentaires = new HashSet<>();

    @OneToMany(mappedBy = "demande")
    private Set<Notation> notations = new HashSet<>();

    @OneToMany(mappedBy = "demande")
    private Set<PieceJointe> pieceJointes = new HashSet<>();
}
