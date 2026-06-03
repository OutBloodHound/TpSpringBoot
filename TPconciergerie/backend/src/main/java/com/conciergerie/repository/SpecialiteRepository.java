package com.conciergerie.repository;

import com.conciergerie.entity.Specialite;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SpecialiteRepository extends JpaRepository<Specialite, Long> {
    Optional<Specialite> findByLibelle(String Libelle);
}
