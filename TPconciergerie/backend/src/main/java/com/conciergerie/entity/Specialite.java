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
@Table(name = "specialite", uniqueConstraints = @UniqueConstraint(columnNames = "libelle"))
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class Specialite implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 100)
    private String libelle;

    @ManyToMany(mappedBy = "specialites")
    private Set<Prestataire> prestataires = new HashSet<>();
}
