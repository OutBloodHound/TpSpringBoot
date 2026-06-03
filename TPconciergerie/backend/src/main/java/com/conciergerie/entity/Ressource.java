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
@Table(name = "ressource")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class Ressource implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 150)
    private String nom;

    @Enumerated(EnumType.STRING)
    @Column(name = "type_ressource", nullable = false, length = 10)
    private TypeRessource typeRessource;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column
    private Integer capacite;

    @Column(nullable = false)
    private boolean actif = true;

    @OneToMany(mappedBy = "ressource")
    private Set<Reservation> reservations = new HashSet<>();
}
