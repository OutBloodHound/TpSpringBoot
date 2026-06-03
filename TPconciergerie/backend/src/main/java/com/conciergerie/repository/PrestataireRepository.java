package com.conciergerie.repository;

import com.conciergerie.entity.Prestataire;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PrestataireRepository extends JpaRepository<Prestataire, Long> {
    Optional<Prestataire> findByUtilisateurId(Long utilisateurId);
    List<Prestataire> findByDisponibleTrue();
}
