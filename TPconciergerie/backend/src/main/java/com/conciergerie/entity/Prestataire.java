package com.conciergerie.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "prestataire", uniqueConstraints = @UniqueConstraint(columnNames = "utilisateur_id"))
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class Prestataire implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "utilisateur_id", unique = true, nullable = false)
    private Utilisateur utilisateur;

    @Column(name = "zone_geo", length = 255)
    private String zoneGeo;

    @Column(name = "note_moyenne", nullable = false)
    private float noteMoyenne;

    @Column(name = "nb_interventions", nullable = false)
    private int nbInterventions;

    @Column(nullable = false)
    private boolean disponible = true;

    @OneToMany(mappedBy = "prestataire")
    private Set<Demande> demandes = new HashSet<>();

    @ManyToMany
    @JoinTable(name = "prestataire_specialite",
            joinColumns = @JoinColumn(name = "prestataire_id"),
            inverseJoinColumns = @JoinColumn(name = "specialite_id"))
    private Set<Specialite> specialites = new HashSet<>();

    @OneToMany(mappedBy = "prestataire")
    private Set<Notation> notations = new HashSet<>();
}
