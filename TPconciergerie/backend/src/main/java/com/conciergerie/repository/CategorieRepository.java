package com.conciergerie.repository;

import com.conciergerie.entity.Categorie;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CategorieRepository extends JpaRepository <Categorie, Long> {
    Optional<Categorie> findByLibelle(String libelle);
}
