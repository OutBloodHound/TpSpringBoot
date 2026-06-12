package com.conciergerie.entity;
// Paquetage des entités JPA — RoleUtilisateur est une énumération, pas une table

public enum RoleUtilisateur {
    // Enum = liste de constantes fixes (jamais modifiables en base)
    // Stockée dans la colonne "role" de la table "utilisateur"
    // Grâce à @Enumerated(EnumType.STRING), le nom exact ("ADMIN") est stocké en DB

    ADMIN,       // Super-administrateur — accès total à l'application
    CONCIERGE,   // Employé de la conciergerie — gère les demandes et prestataires
    PRESTATAIRE, // Prestataire de service — reçoit et traite les demandes
    CLIENT       // Client — crée des demandes et consulte leur suivi
}
