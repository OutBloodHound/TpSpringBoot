package com.conciergerie.mapper;
// Paquetage des mappers MapStruct
// MapStruct = générateur de code qui convertit automatiquement Entity ↔ DTO
// Tout est fait à la compilation (pas de réflexion, pas de perte de performance)

import com.conciergerie.dto.UtilisateurDTO;
import com.conciergerie.entity.Utilisateur;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
// @Mapper = annotation MapStruct qui déclare une interface de mapping
// @Mapping = règle personnalisée pour un champ spécifique

@Mapper
// = "Cette interface est un mapper MapStruct"
// À la compilation, MapStruct génère automatiquement une classe UtilisateurMapperImpl
// Avec le code de conversion pour chaque méthode

@Mapper(componentModel = "spring")  // <-- ATTENTION : manquant ici
// Si on ajoute componentModel = "spring", MapStruct génère un bean Spring (@Component)
// Sinon il faut l'instancier manuellement
// Ici, il manque cette config → @Mapper sans paramètre = pas de gestion Spring auto
// (Vérifie que ça marche : soit l'application a une config globale dans pom.xml)

public interface UtilisateurMapper {
    // MapStruct compare les noms des champs entre entité et DTO
    // Si les noms correspondent → mapping automatique, pas besoin de @Mapping

    UtilisateurDTO toDto(Utilisateur entity);
    // Convertit Utilisateur → UtilisateurDTO
    // Champs identiques (nom, prenom, email...) → mappés automatiquement
    // Champ motDePasse = ignoré car inexistant dans le DTO
    // Relations OneToMany = ignorées car inexistantes dans le DTO
    // C'est automatique : MapStruct ne mappe que ce qui existe des deux côtés

    @Mapping(target = "motDePasse", ignore = true)
    // ignore = true → on ne mappe PAS le motDePasse depuis le DTO
    // (le DTO ne contient pas de motDePasse, ce serait vide/nul)
    // Le mot de passe est passé séparément dans UtilisateurService.create()

    @Mapping(target = "creeLe", ignore = true)
    // La date de création est générée automatiquement par le service

    @Mapping(target = "clients", ignore = true)
    @Mapping(target = "prestataires", ignore = true)
    @Mapping(target = "commentaires", ignore = true)
    @Mapping(target = "notifications", ignore = true)
    // On ignore les 4 relations @OneToMany
    // Car le DTO ne les contient pas — elles sont chargées séparément si besoin

    Utilisateur toEntity(UtilisateurDTO dto);
    // Convertit UtilisateurDTO → Utilisateur
    // Tous les champs simples (nom, prenom, email...) sont mappés automatiquement
    // Les champs marqués ignore=true restent à leur valeur par défaut (null / empty set)
}
