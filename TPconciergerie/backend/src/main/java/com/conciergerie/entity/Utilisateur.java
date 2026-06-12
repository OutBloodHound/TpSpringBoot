package com.conciergerie.entity;
// Paquetage des entités JPA — chaque classe = une table en base de données

import jakarta.persistence.*;
// Jakarta Persistence = l'API standard Java pour mapper des objets Java à des tables SQL
// @Entity, @Table, @Column, @Id, @GeneratedValue, @OneToMany, @Enumerated...

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
// Lombok : génère automatiquement le getter, setter, constructeurs vides et avec tous les args
// Évite d'écrire du code répétitif

import java.io.Serializable;
// Interface "marqueur" : indique que les instances peuvent être sérialisées
// (transformées en flux d'octets, utile pour les sessions HTTP / cache)

import java.time.LocalDateTime;
// Type Java 8+ pour stocker une date/heure sans fuseau horaire
// Correspond au type MySQL DATETIME

import java.util.HashSet;
import java.util.Set;
// Set = collection sans doublons. HashSet = implémentation performante
// Utilisé pour les relations @OneToMany (évite les List qui posent des problèmes avec JPA)

@Entity
// = "Cette classe est une entité JPA, donc une table dans la base de données"
// Chaque champ non-transient deviendra une colonne

@Table(name = "utilisateur", uniqueConstraints = @UniqueConstraint(columnNames = "email"))
// name = "utilisateur" → nom de la table en MySQL (par défaut ce serait "Utilisateur")
// uniqueConstraints = on interdit les doublons sur la colonne "email" au niveau SQL

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
// @Getter = Lombok génère les getters (getId(), getNom()...)
// @Setter = Lombok génère les setters (setId(), setNom()...)
// @NoArgsConstructor = constructeur sans argument (obligatoire pour JPA)
// @AllArgsConstructor = constructeur avec tous les arguments

public class Utilisateur implements Serializable {
    // implements Serializable = optionnel mais recommandé pour une entité JPA
    // Permet de sérialiser l'objet (le transformer en bytes) si besoin

    @Id
    // = "Ce champ est la clé primaire de la table"

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    // = "La valeur est générée automatiquement par MySQL (AUTO_INCREMENT)"
    // IDENTITY = délègue l'auto-incrémentation à MySQL (le plus performant)

    private Long id;
    // Long (classe wrapper) plutôt que long (primitif) pour pouvoir être null
    // Avant le save(), un nouvel utilisateur a id=null, après save() il reçoit l'ID MySQL

    @Column(nullable = false, length = 100)
    // nullable = false → la colonne est NOT NULL en SQL
    // length = 100 → VARCHAR(100) en MySQL

    private String nom;

    @Column(nullable = false, length = 100)
    private String prenom;

    @Column(nullable = false, unique = true, length = 255)
    // unique = true → contrainte SQL UNIQUE, pas deux utilisateurs avec le même email
    // 255 = taille max d'un email standard

    private String email;

    @Column(name = "mot_de_passe", nullable = false, length = 255)
    // name = "mot_de_passe" → le nom de la colonne dans la table (snake_case)
    // En Java on utilise camelCase (motDePasse), JPA fait le lien avec le name

    private String motDePasse;
    // Stocke le hash du mot de passe (BCrypt), jamais le mot de passe en clair !

    @Enumerated(EnumType.STRING)
    // = "Stocke l'enum en tant que chaîne de caractères ('ADMIN')"
    // Alternative : EnumType.ORDINAL stockerait 0, 1, 2, 3 (fragile si on ajoute un rôle)

    @Column(nullable = false, length = 11)
    // length = 11 car "PRESTATAIRE" fait 11 lettres (le plus long des rôles)

    private RoleUtilisateur role;
    // Voir RoleUtilisateur.java : ADMIN, CONCIERGE, PRESTATAIRE, CLIENT

    @Column(length = 20)
    // nullable = true (valeur par défaut), le téléphone est optionnel

    private String telephone;

    @Column(nullable = false)
    private boolean actif = true;
    // Champ booléen : compte actif ou désactivé
    // = true → par défaut un nouvel utilisateur est actif

    @Column(name = "cree_le", nullable = false, updatable = false)
    // updatable = false → une fois créé, on ne peut plus modifier cette date
    // C'est la date d'inscription, elle ne change jamais

    private LocalDateTime creeLe;
    // Rempli automatiquement dans le service avec LocalDateTime.now()

    // ══════════════════════════════════════════════
    // RELATIONS @OneToMany (Un utilisateur → plusieurs X)
    // ══════════════════════════════════════════════

    @OneToMany(mappedBy = "utilisateur")
    // @OneToMany = un utilisateur peut avoir plusieurs clients
    // mappedBy = "utilisateur" → le champ "utilisateur" dans l'entité Client fait le lien
    // Cela signifie que la clé étrangère est dans la table client (porteur de la relation)

    private Set<Client> clients = new HashSet<>();
    // Set = pas de doublons (contrairement à List)
    // new HashSet<>() = initialisation à vide pour éviter les NullPointerException

    @OneToMany(mappedBy = "utilisateur")
    private Set<Prestataire> prestataires = new HashSet<>();
    // Un utilisateur peut être prestataire (ou en avoir plusieurs ?)
    // En réalité: un Utilisateur → un Prestataire (relation OneToOne dans l'autre sens)

    @OneToMany(mappedBy = "utilisateur")
    private Set<Commentaire> commentaires = new HashSet<>();
    // Un utilisateur peut écrire plusieurs commentaires

    @OneToMany(mappedBy = "utilisateur")
    private Set<Notification> notifications = new HashSet<>();
    // Un utilisateur peut recevoir plusieurs notifications
}
