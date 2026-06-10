# TP Conciergerie — State du projet

## Stack technique

### Backend
| Technologie | Version |
|---|---|
| Java | 17 |
| Spring Boot | 3.2.5 |
| Maven | 3.9.16 (global) |
| Base de données | MySQL 8.0 |
| Migration BDD | Flyway |
| ORM | JPA / Hibernate (MySQL8Dialect) |
| Mapping DTO | MapStruct 1.5.5.Final |
| Boilerplate | Lombok 1.18.32 |
| Auth | JJWT 0.12.5 (HS256) |
| Documentation API | SpringDoc OpenAPI 2.5.0 / Swagger UI |
| Validation | Jakarta Bean Validation |
| Monitoring | Spring Actuator |
| Mail | Spring Mail (MailHog/Mailtrap dev) |
| Base JAR | `com.conciergerie:conciergerie-api:1.0.0-SNAPSHOT` |

### Frontend
| Technologie | Version |
|---|---|
| Node | 20-alpine (Docker build) |
| Framework | React (Vite) |
| Serveur statique | Nginx stable-alpine |
| Port dev | 3000 |

### Docker
| Service | Image | Port |
|---|---|---|
| MySQL | mysql:8.0 | 3306 |
| phpMyAdmin | phpmyadmin:latest | 8081 (dev only) |
| Backend | eclipse-temurin:17-jdk-alpine | 8080 |
| Frontend | node:20-alpine → nginx:stable-alpine | 3000 |

### Configuration (application.yml)
- Profil actif : `dev` (variable `SPRING_PROFILES_ACTIVE`)
- JPA : `ddl-auto: validate` (Flyway gère le schéma)
- Upload : 5MB max
- CORS : `http://localhost:3000`
- Swagger : `/swagger-ui.html` / `/api-docs`
- JWT secret : 32+ caractères

---

## Architecture — Flux des données

```
Navigator (React)                http://localhost:3000
        │  Requêtes HTTP (fetch/axios)
        ▼
CONTROLLERS  ← squelettes vides actuellement
  Rôle : recevoir la requête, appeler le Service, retourner le DTO en JSON
        │
        ▼
┌──────────────────────────────────────────────────┐
│ SECURITY                                         │
│  JwtUtil      ← générer/valider JWT              │
│  JwtFilter    ← intercepter requêtes (Bearer)    │
│  SecurityConfig ← routes publiques/protégées     │
│  CorsConfig   ← autoriser le frontend React      │
└──────────────────────────────────────────────────┘
        │
        ▼
SERVICES  (logique métier)
  Chaque service = CRUD :
  findAll() → findById() → create() → update() → delete()
        │
        ▼
MAPPER (MapStruct)  ← convertit Entity ↔ DTO
        │
        ▼
REPOSITORY (Spring Data JPA)  ← requêtes SQL
        │
        ▼
BASE DE DONNÉES  MySQL 8.0

Schéma de dépendance : Controller → Service → Mapper → Repository → DB
                                           ↘
                                           DTO
Toutes les réponses sont en JSON. Base path : /api
```

---

## ✅ Fait

### entity/ (12 entités + 6 enums)
- `Utilisateur` — id, nom, prenom, email, motDePasse, role, telephone, actif, creeLe
- `Client` — id, utilisateur (OneToOne), adresse, typeClient, batiment, etage
- `Prestataire` — id, utilisateur (OneToOne), zoneGeo, noteMoyenne, nbInterventions, disponible
- `Demande` — id, titre, description, statut, priorite, client, prestataire, categorie, creeLe, clotureLe
- `Reservation` — id, client, ressource, dateDebut, dateFin, statut, notes
- `Notification` — id, utilisateur, type, message, lien, lu, creeLe
- `Categorie` — id, libelle (unique), description, icone, couleur, delaiSlaHeures
- `Specialite` — id, libelle (unique)
- `Ressource` — id, nom, typeRessource, description, capacite, actif
- `PieceJointe` — id, demande, nomFichier, chemin, mimeType, tailleOctets, creeLe
- `Notation` — id, demande (unique), prestataire, note, commentaire, creeLe
- `Commentaire` — id, demande, utilisateur, texte, interne, creeLe
- Enums : `RoleUtilisateur` (ADMIN/CONCIERGE/PRESTATAIRE/CLIENT), `Statut`, `Priorite`, `StatutReservation`, `TypeClient`, `TypeRessource`

### repository/ (12 interfaces)
- `UtilisateurRepository` — `findByEmail(String): Optional`
- `ClientRepository` — `findByUtilisateurId(Long): Optional`
- `PrestataireRepository` — `findByUtilisateurId(Long): Optional`, `findByDisponibleTrue(): List`
- `DemandeRepository` — `findByClientId(Long): List`, `findByPrestataireId(Long): List`, `findByStatut(Statut): List`
- `ReservationRepository` — `findByClientId(Long): List`, `findByRessourceId(Long): List`, `findByStatut(StatutReservation): List`
- `CategorieRepository` — `findByLibelle(String): Optional`
- `SpecialiteRepository` — `findByLibelle(String): Optional`
- `RessourceRepository` — `findByTypeRessource(TypeRessource): List`, `findByActifTrue(): List`
- `NotificationRepository` — `findByUtilisateurId(Long): List`, `findByUtilisateurIdAndLuFalse(Long): List`
- `PieceJointeRepository` — `findByDemandeId(Long): List`
- `NotationRepository` — `findByDemandeId(Long): List`, `findByPrestataireId(Long): List`
- `CommentaireRepository` — `findByDemandeId(Long): List`

### dto/ (12 classes, Lombok @Data)
Chaque entité a son XxxDTO. Relations ManyToOne → `Long xxxId` dans le DTO.
- `ClientDTO` : utilisateurId
- `DemandeDTO` : clientId, prestataireId, categorieId
- `ReservationDTO` : clientId, ressourceId
- `CommentaireDTO` : demandeId, utilisateurId
- `NotationDTO` : demandeId, prestataireId
- `NotificationDTO` : utilisateurId
- `PieceJointeDTO` : demandeId
- `PrestataireDTO` : utilisateurId
- Autres : pas de FK nécessaire (Categorie, Specialite, Ressource, Utilisateur)

### mapper/ (12 interfaces MapStruct — toutes terminées)
- `UtilisateurMapper` — ignore motDePasse + 4 relations
- `ClientMapper` — ignore utilisateur ; mappe `utilisateur.id` → `utilisateurId`
- `PrestataireMapper` — ignore utilisateur, specialites, notations ; mappe `utilisateur.id` → `utilisateurId`
- `DemandeMapper` — ignore client, prestataire, categorie, commentaires, notations, pieceJointes ; mappe les IDs
- `ReservationMapper` — ignore client, ressource ; mappe `client.id` → `clientId`, `ressource.id` → `ressourceId`
- `CategorieMapper` — ignore demandes
- `NotificationMapper` — ignore utilisateur ; mappe `utilisateur.id` → `utilisateurId`
- `RessourceMapper` — ignore reservations
- `SpecialiteMapper` — ignore prestataires
- `PieceJointeMapper` — ignore demande ; mappe `demande.id` → `demandeId`
- `NotationMapper` — ignore demande, prestataire ; mappe `demande.id` → `demandeId`, `prestataire.id` → `prestataireId`
- `CommentaireMapper` — ignore demande, utilisateur ; mappe `demande.id` → `demandeId`, `utilisateur.id` → `utilisateurId`
- 
---
### service/ (12 services — tous terminés)
- `ClientService` — CRUD + FK utilisateur
- `UtilisateurService` — CRUD + hash motDePasse
- `DemandeService` — CRUD + FK client, categorie, prestataire (optionnel)
- `PrestataireService` — CRUD + FK utilisateur
- `CategorieService` — CRUD simple (pas de FK)
- `SpecialiteService` — CRUD simple (pas de FK)
- `RessourceService` — CRUD simple (pas de FK)
- `ReservationService` — CRUD + FK client, ressource
- `CommentaireService` — CRUD + FK demande, utilisateur
- `NotationService` — CRUD + FK demande, prestataire
- `NotificationService` — CRUD + FK utilisateur
- `PieceJointeService` — CRUD + FK demande
- 

## ⬜ À faire

### security/ (JWT)
- `JwtUtil.java` ✅ — génère et valide les tokens (access + refresh)
- `JwtFilter.java` ⬜ — intercepte les requêtes et vérifie le token
- `SecurityConfig.java` ⬜ — configure les routes publiques/protégées, CORS, BCrypt

### config/
- `CorsConfig.java` ⬜ — à créer (actuellement un squelette vide `CorsConfing.java`)
- Renommer `CorsConfing.java` → `CorsConfig.java` (typo)

### exception/
- `GlobalExceptionHandler.java` 🟡 — ne gère que `ResourceNotFoundException`
- À ajouter : validation errors (400), authentification errors (401), générique (500)

### controller/ (endpoints REST)
- 5 squelettes vides existent : `AuthController`, `DemandeController`, `NotificationController`, `PrestataireController`, `ReservationController`
- 7 à créer : `CategorieController`, `CommentaireController`, `NotationController`, `PieceJointeController`, `RessourceController`, `SpecialiteController`, `UtilisateurController`

### Flyway
- Configuré dans `application.yml` mais aucune migration écrite
- Actuellement le schéma est bootstrappé via `init.sql`

### Tests
- Aucun test écrit (dépendances présentes dans le pom.xml : `spring-boot-starter-test`, `h2`)

---

## URLs
| Service | URL |
|---|---|
| API REST | `http://localhost:8080` |
| Swagger UI | `http://localhost:8080/swagger-ui.html` |
| API Docs | `http://localhost:8080/api-docs` |
| Frontend | `http://localhost:3000` |
| phpMyAdmin | `http://localhost:8081` |

## Commandes utiles
```bash
# Lancer tout le projet (dev)
docker compose --profile dev up -d

# Lancer phpMyAdmin uniquement
docker compose --profile dev up -d phpmyadmin

# Compiler le backend
cd backend && mvn clean compile -DskipTests

# Build du backend sans Docker
cd backend && mvn clean package -DskipTests

# Lancer le backend sans Docker
cd backend && mvn spring-boot:run
```

## Fonctionnement

je suis la pour apprendre j'attends des explication et un guide du projet
que rien ne soit fait sans etre expliqué