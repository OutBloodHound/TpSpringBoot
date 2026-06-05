# TP Conciergerie — State du projet

## Stack technique

### Backend
| Technologie | Version |
|---|---|
| Java | 17 |
| Spring Boot | 3.2.5 |
| Maven | Wrapper (./mvnw) |
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

## Architecture

```
Entity → Repository → DTO → Mapper → Service → Controller
base path: /api
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
- `PieceJointeRepository` — à faire
- `NotationRepository` — à faire
- `CommentaireRepository` — à faire

### dto/ (12 classes, Lombok @Data)
Chaque entité a son XxxDTO. Relations ManyToOne → `Long xxxId` dans le DTO.
- `ClientDTO` : utilisateurId
- `DemandeDTO` : clientId, prestataireId, categorieId
- `ReservationDTO` : clientId, ressourceId
- `CommentaireDTO` : demandeId, utilisateurId
- `NotationDTO` : demandeId, prestataireId
- Autres : pas de FK nécessaire (Categorie, Specialite, Ressource, PieceJointe, Notification, Prestataire, Utilisateur)

### mapper/ (9 interfaces MapStruct sur 12)
- `UtilisateurMapper` ✅ — ignore motDePasse + 4 relations
- `ClientMapper` ✅ — ignore utilisateur
- `PrestataireMapper` ✅ — ignore utilisateur, specialites, notations
- `DemandeMapper` ✅ — ignore client, prestataire, categorie, commentaires, notations, pieceJointes
- `ReservationMapper` ✅ — ignore client, ressource
- `CategorieMapper` ✅ — ignore demandes
- `NotificationMapper` ✅ — ignore utilisateur
- `RessourceMapper` ✅ — ignore reservations
- `SpecialiteMapper` ✅ — ignore prestataires
- `PieceJointeMapper` ✅ à faire
- `NotationMapper` ✅ à faire
- `CommentaireMapper` ✅ à faire

---

## ⬜ À faire

### service/ (logique métier)
### controller/ (endpoints REST — squelettes existent mais vides)
### security/ (JWT — JwtFilter, JwtUtil, SecurityConfig vides)
### config/ (CorsConfig à remplir)
### exception/ (GlobalExceptionHandler à remplir)

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

# Build du backend sans Docker
cd backend && ./mvnw clean package -DskipTests

# Lancer le backend sans Docker
cd backend && ./mvnw spring-boot:run
```

## Fonctionnement

je suis la pour apprendre j'attends des explication et un guide du projet
que rien ne soit fait sans etre expliqué