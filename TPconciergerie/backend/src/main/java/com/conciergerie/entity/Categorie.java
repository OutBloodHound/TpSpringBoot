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
@Table(name = "categorie", uniqueConstraints = @UniqueConstraint(columnNames = "libelle"))
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class Categorie implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 100)
    private String libelle;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(length = 80)
    private String icone;

    @Column(length = 7)
    private String couleur;

    @Column(name = "delai_sla_heures", nullable = false)
    private int delaiSlaHeures;

    @OneToMany(mappedBy = "categorie")
    private Set<Demande> demandes = new HashSet<>();
}
