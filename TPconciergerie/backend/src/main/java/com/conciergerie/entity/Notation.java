package com.conciergerie.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Table(name = "notation", uniqueConstraints = @UniqueConstraint(columnNames = "demande_id"))
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class Notation implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "demande_id", unique = true, nullable = false)
    private Demande demande;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "prestataire_id", nullable = false)
    private Prestataire prestataire;

    @Column(nullable = false)
    private byte note;

    @Column(columnDefinition = "TEXT")
    private String commentaire;

    @Column(name = "cree_le", nullable = false, updatable = false)
    private LocalDateTime creeLe;
}
