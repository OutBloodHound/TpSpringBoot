-- =============================================================
--  ConciergeApp — Script d'initialisation MySQL
--  Version : 1.0
-- =============================================================

CREATE DATABASE IF NOT EXISTS conciergerie
  CHARACTER SET utf8mb4
  COLLATE utf8mb4_unicode_ci;

USE conciergerie;

-- -------------------------------------------------------------
-- TABLE : utilisateur
-- Entité centrale. Tous les rôles héritent de cette table.
-- -------------------------------------------------------------
CREATE TABLE utilisateur (
    id            INT          NOT NULL AUTO_INCREMENT,
    nom           VARCHAR(100) NOT NULL,
    prenom        VARCHAR(100) NOT NULL,
    email         VARCHAR(255) NOT NULL,
    mot_de_passe  VARCHAR(255) NOT NULL,          -- hash bcrypt
    role          ENUM('ADMIN','CONCIERGE','PRESTATAIRE','CLIENT') NOT NULL,
    telephone     VARCHAR(20),
    actif         TINYINT(1)   NOT NULL DEFAULT 1,
    cree_le       DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT pk_utilisateur PRIMARY KEY (id),
    CONSTRAINT uq_utilisateur_email UNIQUE (email)
);

CREATE INDEX idx_utilisateur_role  ON utilisateur(role);
CREATE INDEX idx_utilisateur_actif ON utilisateur(actif);

-- -------------------------------------------------------------
-- TABLE : client
-- Profil étendu pour le rôle CLIENT.
-- -------------------------------------------------------------
CREATE TABLE client (
    id               INT         NOT NULL AUTO_INCREMENT,
    utilisateur_id   INT         NOT NULL,
    adresse          VARCHAR(255),
    type_client      ENUM('RESIDENT','HOTEL','ENTREPRISE','PARTICULIER') NOT NULL DEFAULT 'PARTICULIER',
    batiment         VARCHAR(100),
    etage            VARCHAR(20),
    CONSTRAINT pk_client           PRIMARY KEY (id),
    CONSTRAINT uq_client_util      UNIQUE      (utilisateur_id),
    CONSTRAINT fk_client_util      FOREIGN KEY (utilisateur_id)
                                   REFERENCES  utilisateur(id)
                                   ON DELETE CASCADE
);

-- -------------------------------------------------------------
-- TABLE : specialite
-- Catalogue des spécialités métier (plomberie, électricité…)
-- -------------------------------------------------------------
CREATE TABLE specialite (
    id       INT          NOT NULL AUTO_INCREMENT,
    libelle  VARCHAR(100) NOT NULL,
    CONSTRAINT pk_specialite        PRIMARY KEY (id),
    CONSTRAINT uq_specialite_libelle UNIQUE (libelle)
);

-- -------------------------------------------------------------
-- TABLE : prestataire
-- Profil étendu pour le rôle PRESTATAIRE.
-- -------------------------------------------------------------
CREATE TABLE prestataire (
    id                INT          NOT NULL AUTO_INCREMENT,
    utilisateur_id    INT          NOT NULL,
    zone_geo          VARCHAR(255),
    note_moyenne      FLOAT        NOT NULL DEFAULT 0,
    nb_interventions  INT          NOT NULL DEFAULT 0,
    disponible        TINYINT(1)   NOT NULL DEFAULT 1,
    CONSTRAINT pk_prestataire      PRIMARY KEY (id),
    CONSTRAINT uq_prestataire_util UNIQUE      (utilisateur_id),
    CONSTRAINT fk_prestataire_util FOREIGN KEY (utilisateur_id)
                                   REFERENCES  utilisateur(id)
                                   ON DELETE CASCADE
);

CREATE INDEX idx_prestataire_dispo ON prestataire(disponible);

-- -------------------------------------------------------------
-- TABLE : prestataire_specialite  (jointure N-N)
-- -------------------------------------------------------------
CREATE TABLE prestataire_specialite (
    prestataire_id  INT NOT NULL,
    specialite_id   INT NOT NULL,
    CONSTRAINT pk_presta_spe  PRIMARY KEY (prestataire_id, specialite_id),
    CONSTRAINT fk_ps_presta   FOREIGN KEY (prestataire_id)
                              REFERENCES  prestataire(id) ON DELETE CASCADE,
    CONSTRAINT fk_ps_spe      FOREIGN KEY (specialite_id)
                              REFERENCES  specialite(id)  ON DELETE CASCADE
);

-- -------------------------------------------------------------
-- TABLE : categorie
-- Catégories de demandes avec SLA associé.
-- -------------------------------------------------------------
CREATE TABLE categorie (
    id                INT          NOT NULL AUTO_INCREMENT,
    libelle           VARCHAR(100) NOT NULL,
    description       TEXT,
    icone             VARCHAR(80),
    couleur           VARCHAR(7),                  -- code hex ex: #FF5733
    delai_sla_heures  INT          NOT NULL DEFAULT 24,
    CONSTRAINT pk_categorie         PRIMARY KEY (id),
    CONSTRAINT uq_categorie_libelle UNIQUE (libelle)
);

-- -------------------------------------------------------------
-- TABLE : demande
-- Entité métier principale.
-- -------------------------------------------------------------
CREATE TABLE demande (
    id              INT      NOT NULL AUTO_INCREMENT,
    titre           VARCHAR(255) NOT NULL,
    description     TEXT,
    statut          ENUM('EN_ATTENTE','EN_COURS','TERMINEE','ANNULEE') NOT NULL DEFAULT 'EN_ATTENTE',
    priorite        ENUM('BASSE','NORMALE','HAUTE','URGENTE')          NOT NULL DEFAULT 'NORMALE',
    client_id       INT      NOT NULL,
    prestataire_id  INT,                           -- NULL tant que non affectée
    categorie_id    INT      NOT NULL,
    cree_le         DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    cloture_le      DATETIME,
    CONSTRAINT pk_demande       PRIMARY KEY (id),
    CONSTRAINT fk_dem_client    FOREIGN KEY (client_id)
                                REFERENCES  client(id),
    CONSTRAINT fk_dem_presta    FOREIGN KEY (prestataire_id)
                                REFERENCES  prestataire(id)
                                ON DELETE SET NULL,
    CONSTRAINT fk_dem_categorie FOREIGN KEY (categorie_id)
                                REFERENCES  categorie(id)
);

CREATE INDEX idx_demande_statut    ON demande(statut);
CREATE INDEX idx_demande_priorite  ON demande(priorite);
CREATE INDEX idx_demande_client    ON demande(client_id);
CREATE INDEX idx_demande_presta    ON demande(prestataire_id);
CREATE INDEX idx_demande_cree_le   ON demande(cree_le);

-- -------------------------------------------------------------
-- TABLE : commentaire
-- Commentaires internes ou visibles sur une demande.
-- -------------------------------------------------------------
CREATE TABLE commentaire (
    id              INT      NOT NULL AUTO_INCREMENT,
    demande_id      INT      NOT NULL,
    utilisateur_id  INT      NOT NULL,
    texte           TEXT     NOT NULL,
    interne         TINYINT(1) NOT NULL DEFAULT 0,   -- 1 = visible concierge/admin seulement
    cree_le         DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT pk_commentaire    PRIMARY KEY (id),
    CONSTRAINT fk_com_demande    FOREIGN KEY (demande_id)
                                 REFERENCES  demande(id) ON DELETE CASCADE,
    CONSTRAINT fk_com_utilisateur FOREIGN KEY (utilisateur_id)
                                  REFERENCES  utilisateur(id)
);

CREATE INDEX idx_commentaire_demande ON commentaire(demande_id);

-- -------------------------------------------------------------
-- TABLE : notation
-- Évaluation du prestataire après clôture d'une demande.
-- -------------------------------------------------------------
CREATE TABLE notation (
    id              INT         NOT NULL AUTO_INCREMENT,
    demande_id      INT         NOT NULL,
    prestataire_id  INT         NOT NULL,
    note            TINYINT     NOT NULL,             -- valeur 1 à 5
    commentaire     TEXT,
    cree_le         DATETIME    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT pk_notation        PRIMARY KEY (id),
    CONSTRAINT uq_notation_demande UNIQUE (demande_id),  -- une seule note par demande
    CONSTRAINT chk_notation_note  CHECK (note BETWEEN 1 AND 5),
    CONSTRAINT fk_not_demande     FOREIGN KEY (demande_id)
                                  REFERENCES  demande(id) ON DELETE CASCADE,
    CONSTRAINT fk_not_presta      FOREIGN KEY (prestataire_id)
                                  REFERENCES  prestataire(id)
);

CREATE INDEX idx_notation_presta ON notation(prestataire_id);

-- -------------------------------------------------------------
-- TABLE : ressource
-- Ressources réservables (salles, véhicules, équipements…)
-- -------------------------------------------------------------
CREATE TABLE ressource (
    id             INT          NOT NULL AUTO_INCREMENT,
    nom            VARCHAR(150) NOT NULL,
    type_ressource ENUM('SALLE','VEHICULE','EQUIPEMENT','SERVICE') NOT NULL,
    description    TEXT,
    capacite       INT,
    actif          TINYINT(1)   NOT NULL DEFAULT 1,
    CONSTRAINT pk_ressource PRIMARY KEY (id)
);

CREATE INDEX idx_ressource_type  ON ressource(type_ressource);
CREATE INDEX idx_ressource_actif ON ressource(actif);

-- -------------------------------------------------------------
-- TABLE : reservation
-- Réservation d'une ressource par un client.
-- -------------------------------------------------------------
CREATE TABLE reservation (
    id           INT      NOT NULL AUTO_INCREMENT,
    client_id    INT      NOT NULL,
    ressource_id INT      NOT NULL,
    date_debut   DATETIME NOT NULL,
    date_fin     DATETIME NOT NULL,
    statut       ENUM('EN_ATTENTE','CONFIRMEE','ANNULEE') NOT NULL DEFAULT 'EN_ATTENTE',
    notes        TEXT,
    CONSTRAINT pk_reservation      PRIMARY KEY (id),
    CONSTRAINT chk_resa_dates      CHECK (date_fin > date_debut),
    CONSTRAINT fk_resa_client      FOREIGN KEY (client_id)
                                   REFERENCES  client(id),
    CONSTRAINT fk_resa_ressource   FOREIGN KEY (ressource_id)
                                   REFERENCES  ressource(id)
);

CREATE INDEX idx_reservation_client    ON reservation(client_id);
CREATE INDEX idx_reservation_ressource ON reservation(ressource_id);
CREATE INDEX idx_reservation_dates     ON reservation(date_debut, date_fin);

-- -------------------------------------------------------------
-- TABLE : notification
-- Notifications in-app par utilisateur.
-- -------------------------------------------------------------
CREATE TABLE notification (
    id              INT          NOT NULL AUTO_INCREMENT,
    utilisateur_id  INT          NOT NULL,
    type            VARCHAR(60)  NOT NULL,           -- ex: DEMANDE_AFFECTEE, STATUT_CHANGE…
    message         TEXT         NOT NULL,
    lien            VARCHAR(255),
    lu              TINYINT(1)   NOT NULL DEFAULT 0,
    cree_le         DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT pk_notification    PRIMARY KEY (id),
    CONSTRAINT fk_notif_util      FOREIGN KEY (utilisateur_id)
                                  REFERENCES  utilisateur(id) ON DELETE CASCADE
);

CREATE INDEX idx_notif_util_lu ON notification(utilisateur_id, lu);

-- -------------------------------------------------------------
-- TABLE : piece_jointe
-- Fichiers attachés aux demandes.
-- -------------------------------------------------------------
CREATE TABLE piece_jointe (
    id            INT          NOT NULL AUTO_INCREMENT,
    demande_id    INT          NOT NULL,
    nom_fichier   VARCHAR(255) NOT NULL,
    chemin        VARCHAR(500) NOT NULL,
    mime_type     VARCHAR(100) NOT NULL,
    taille_octets INT          NOT NULL,
    cree_le       DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT pk_piece_jointe  PRIMARY KEY (id),
    CONSTRAINT fk_pj_demande    FOREIGN KEY (demande_id)
                                REFERENCES  demande(id) ON DELETE CASCADE
);

CREATE INDEX idx_pj_demande ON piece_jointe(demande_id);

-- =============================================================
-- DONNÉES DE RÉFÉRENCE
-- =============================================================

INSERT INTO specialite (libelle) VALUES
  ('Plomberie'),
  ('Électricité'),
  ('Climatisation / CVC'),
  ('Serrurerie'),
  ('Nettoyage'),
  ('Jardinage'),
  ('Peinture'),
  ('Informatique'),
  ('Sécurité'),
  ('Livraison');

INSERT INTO categorie (libelle, description, icone, couleur, delai_sla_heures) VALUES
  ('Maintenance',      'Réparations et entretien courant',       'ti-tool',          '#378ADD', 24),
  ('Nettoyage',        'Nettoyage des espaces communs ou privés','ti-sparkles',       '#1D9E75', 8),
  ('Sécurité',         'Incidents et contrôle d'accès',          'ti-shield-check',  '#D85A30', 4),
  ('Transport',        'Navettes, courses et livraisons',        'ti-car',           '#7F77DD', 48),
  ('Réservation',      'Demandes de réservation de ressources',  'ti-calendar-event','#BA7517', 12),
  ('Administratif',    'Courrier, badges, documents',            'ti-file-text',     '#888780', 72),
  ('Restauration',     'Commandes et service traiteur',          'ti-coffee',        '#D4537E', 6),
  ('Informatique',     'Support et équipements informatiques',   'ti-device-laptop', '#378ADD', 8);

-- Compte admin par défaut  (mot de passe : Admin1234! — à changer en prod)
INSERT INTO utilisateur (nom, prenom, email, mot_de_passe, role) VALUES
  ('Admin', 'Système', 'admin@conciergerie.local',
   '$2a$12$xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx',
   'ADMIN');
