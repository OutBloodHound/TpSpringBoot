package com.conciergerie.repository;

import com.conciergerie.entity.Notation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NotationRepository extends JpaRepository<Notation, Long> {
    List<Notation> findByDemandeId(Long demandeId);
    List<Notation> findByPrestataireId(Long prestataireId);
}
