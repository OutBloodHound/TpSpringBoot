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
@Table(name = "utilisateur", uniqueConstraints = @UniqueConstraint(columnNames = "email"))
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class Utilisateur implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String nom;

    @Column(nullable = false, length = 100)
    private String prenom;

    @Column(nullable = false, unique = true, length = 255)
    private String email;

    @Column(name = "mot_de_passe", nullable = false, length = 255)
    private String motDePasse;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 11)
    private RoleUtilisateur role;

    @Column(length = 20)
    private String telephone;

    @Column(nullable = false)
    private boolean actif = true;

    @Column(name = "cree_le", nullable = false, updatable = false)
    private LocalDateTime creeLe;

    @OneToMany(mappedBy = "utilisateur")
    private Set<Client> clients = new HashSet<>();

    @OneToMany(mappedBy = "utilisateur")
    private Set<Prestataire> prestataires = new HashSet<>();

    @OneToMany(mappedBy = "utilisateur")
    private Set<Commentaire> commentaires = new HashSet<>();

    @OneToMany(mappedBy = "utilisateur")
    private Set<Notification> notifications = new HashSet<>();
}
