package com.conciergerie.repository;

import com.conciergerie.entity.PieceJointe;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PieceJointRepository extends JpaRepository<PieceJointe, Long> {
    List<PieceJointe> findByDemandeId(Long demandeId);
}
