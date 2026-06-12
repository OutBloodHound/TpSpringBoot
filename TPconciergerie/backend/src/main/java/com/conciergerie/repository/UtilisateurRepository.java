package com.conciergerie.repository;
// Paquetage des repositories = couche d'accès aux données (requêtes SQL)

import com.conciergerie.entity.Utilisateur;
// L'entité que ce repository va manipuler

import org.springframework.data.jpa.repository.JpaRepository;
// Interface fournie par Spring Data JPA
// Contient déjà : findAll(), findById(), save(), deleteById()... tout le CRUD de base

import java.util.Optional;
// Optional = conteneur qui peut être vide ou contenir une valeur
// Évite les NullPointerException : force le développeur à gérer l'absence de résultat

public interface UtilisateurRepository extends JpaRepository<Utilisateur, Long> {
    // JpaRepository<Entité, TypeDeLaCléPrimaire>
    // Héritage = cette interface hérite de TOUTES les méthodes CRUD sans les écrire

    Optional<Utilisateur> findByEmail(String email);
    // Requête dérivée du nom : Spring Data JPA lit "findByEmail" et génère automatiquement :
    //   SELECT * FROM utilisateur WHERE email = ?
    // Pas besoin d'écrire la requête SQL — Spring analyse le nom de la méthode
    // Retourne Optional : si l'email n'existe pas → Optional.empty() (pas de null)
}

