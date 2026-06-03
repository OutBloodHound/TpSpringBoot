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
@Table(name = "client", uniqueConstraints = @UniqueConstraint(columnNames = "utilisateur_id"))
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class Client implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "utilisateur_id", unique = true, nullable = false)
    private Utilisateur utilisateur;

    @Column(length = 255)
    private String adresse;

    @Enumerated(EnumType.STRING)
    @Column(name = "type_client", nullable = false, length = 11)
    private TypeClient typeClient;

    @Column(length = 100)
    private String batiment;

    @Column(length = 20)
    private String etage;

    @OneToMany(mappedBy = "client")
    private Set<Reservation> reservations = new HashSet<>();

    @OneToMany(mappedBy = "client")
    private Set<Demande> demandes = new HashSet<>();
}
