package com.conciergerie.repository;

import com.conciergerie.entity.*;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DemandeRepository extends JpaRepository<Demande, Long> {
    List<Demande> findByClientId(Long clientId);
    List<Demande> findByPrestataireId(Long prestataireId);
    List<Demande> findByStatut(Statut statut);
}
