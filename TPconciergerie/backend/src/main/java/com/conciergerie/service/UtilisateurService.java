package com.conciergerie.service;
// Paquetage des services = logique métier de l'application
// Chaque service = CRUD pour une entité, + éventuelle logique spécifique

import com.conciergerie.dto.UtilisateurDTO;
import com.conciergerie.entity.*;
// Import de toutes les entités (Utilisateur, Client, Prestataire...)
// Utilisé ici pour Utilisateur, mais aussi pour les types dans les signatures

import com.conciergerie.exception.ResourceNotFoundException;
// Exception personnalisée levée quand un utilisateur n'est pas trouvé
// Sera attrapée par GlobalExceptionHandler qui renvoie une réponse 404

import com.conciergerie.mapper.UtilisateurMapper;
import com.conciergerie.repository.UtilisateurRepository;

import lombok.RequiredArgsConstructor;
// Génère un constructeur avec tous les champs "final" en paramètre
// Spring s'en sert pour injecter repository et mapper automatiquement

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
// @Service = indique à Spring que cette classe est un bean de service
// @Transactional = chaque méthode publique est exécutée DANS une transaction SQL

import java.time.LocalDateTime;
import java.util.List;
// List pour les retours de findAll()

@Service
// = "Cette classe est un service Spring" (détectée par le scan de composants)
// Spring va créer un singleton de cette classe et l'injecter là où c'est nécessaire

@RequiredArgsConstructor
// = "Génère un constructeur avec tous les champs final"
// Ici : public UtilisateurService(UtilisateurRepository repository, UtilisateurMapper mapper)
// Spring utilise ce constructeur pour l'injection de dépendances

@Transactional
// = "Toutes les méthodes publiques sont transactionnelles"
// Spring ouvre une connexion JDBC, fait begin/commit/rollback automatiquement
// Si une exception est lancée → rollback (annulation) de toutes les modifications

public class UtilisateurService {
    private final UtilisateurRepository repository;
    // Repository = accès à la base de données (injecté par Spring via le constructeur)

    private final UtilisateurMapper mapper;
    // Mapper = conversion Entity ↔ DTO (injecté par Spring via le constructeur)

    // ══════════════════════════════════════════════
    // findAll() — Récupère tous les utilisateurs
    // ══════════════════════════════════════════════

    public List<UtilisateurDTO> findAll() {
        // Retourne la liste de tous les utilisateurs en DTO (jamais l'entité directement)

        return repository.findAll()
                // JpaRepository.findAll() = génère : SELECT * FROM utilisateur
                // Retourne une List<Utilisateur> (entités brutes)

                .stream()
                // Convertit la liste en Stream pour utiliser map() et toList()

                .map(mapper::toDto)
                // Pour chaque Utilisateur de la liste, appelle mapper.toDto()
                // Convertit chaque entité en DTO (sans motDePasse, sans relations)

                .toList();
                // Collecte le résultat dans une List<UtilisateurDTO> (Java 16+)
                // Avant Java 16, on utilisait .collect(Collectors.toList())
    }

    // ══════════════════════════════════════════════
    // findById() — Récupère un utilisateur par son ID
    // ══════════════════════════════════════════════

    public UtilisateurDTO findById(Long id) {
        // Retourne un seul utilisateur ou lance une exception

        Utilisateur entity = repository.findById(id)
                // JpaRepository.findById(Long) = SELECT * FROM utilisateur WHERE id = ?
                // Retourne un Optional<Utilisateur> (vide si l'ID n'existe pas)

                .orElseThrow(() -> new ResourceNotFoundException("Utilisateur", id));
                // Si Optional est vide → on crée et lance ResourceNotFoundException
                // Le message sera : "Utilisateur non trouvé avec l'id : 42"
                // GlobalExceptionHandler catch ça et renvoie une réponse HTTP 404

        return mapper.toDto(entity);
        // Transforme l'entité en DTO avant de la renvoyer
    }

    // ══════════════════════════════════════════════
    // create() — Crée un nouvel utilisateur
    // ══════════════════════════════════════════════

    public UtilisateurDTO create(UtilisateurDTO dto, String motDePasse) {
        // Paramètres : les infos de base (dto) + le mot de passe (hashé en amont par le controller)
        // Le motDePasse est passé à part car le DTO ne le contient PAS (sécurité)

        Utilisateur entity = mapper.toEntity(dto);
        // Convertit le DTO (provenant du frontend/JSON) en entité JPA
        // Les champs complexes (motDePasse, creeLe, relations) sont ignorés par le mapper

        entity.setMotDePasse(motDePasse);
        // On assigne le mot de Passe manuellement (hashé par BCrypt dans le security layer)
        // Ne JAMAIS stocker de mot de passe en clair

        entity.setActif(true);
        // Par défaut : le compte est actif
        // Pourrait être false si on veut une validation par email avant activation

        entity.setCreeLe(LocalDateTime.now());
        // Date/heure courante : moment de la création du compte
        // updatable = false dans l'entité → cette valeur ne pourra JAMAIS être modifiée

        entity = repository.save(entity);
        // repository.save() fait un INSERT INTO utilisateur
        // Retourne l'entité AVEC l'ID généré par MySQL (AUTO_INCREMENT)
        // On réassigne entity avec le retour pour avoir l'ID

        return mapper.toDto(entity);
        // Convertit l'entité sauvegardée (avec son ID) en DTO et le retourne
        // Le frontend reçoit le DTO avec l'ID pour confirmer la création
    }

    // ══════════════════════════════════════════════
    // update() — Modifie un utilisateur existant
    // ══════════════════════════════════════════════

    public UtilisateurDTO update(Long id, UtilisateurDTO dto) {
        // Paramètres : ID de l'utilisateur à modifier + nouvelles données

        Utilisateur existing = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Utilisateur", id));
                // Vérifie d'abord que l'utilisateur EXISTE en base
                // Si non → 404 tout de suite (pas de update sur un fantôme)

        Utilisateur entity = mapper.toEntity(dto);
        // Convertit le DTO en nouvelle entité (les champs viennent du frontend)
        // À ce stade, entity n'a pas d'ID (null) car le DTO peut avoir id=null

        entity.setId(id);
        // On réassigne l'ID pour que JPA fasse un UPDATE (pas un INSERT)
        // Si l'ID est null → JPA ferait un INSERT (création au lieu de modification)

        entity.setMotDePasse(existing.getMotDePasse());
        // On conserve l'ANCIEN mot de passe (le frontend ne l'envoie pas)
        // Si on ne faisait pas ça, il serait écrasé par null

        entity.setActif(dto.isActif());
        // L'état actif vient du DTO (le frontend peut activer/désactiver)

        entity.setCreeLe(existing.getCreeLe());
        // On conserve la date de création originale (ne doit pas changer)
        // Même si updatable=false dans l'entité, c'est plus sûr de la remettre

        entity = repository.save(entity);
        // Maintenant, save() fait un UPDATE (car l'ID existe déjà en base)
        // MySQL : UPDATE utilisateur SET nom=?, prenom=?, ... WHERE id = ?

        return mapper.toDto(entity);
        // Retourne le DTO mis à jour
    }

    // ══════════════════════════════════════════════
    // delete() — Supprime un utilisateur
    // ══════════════════════════════════════════════

    public void delete(Long id) {
        // Supprime un utilisateur par son ID

        Utilisateur existing = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Utilisateur", id));
                // Vérifie que l'utilisateur existe avant de supprimer
                // Si on appelait deleteById() avec un ID inexistant, Spring lancerait
                // EmptyResultDataAccessException (message moins clair)
                // Grâce à ce check → message "Utilisateur non trouvé avec l'id : X" plus propre

        repository.deleteById(id);
        // JpaRepository.deleteById() = DELETE FROM utilisateur WHERE id = ?
    }
}
