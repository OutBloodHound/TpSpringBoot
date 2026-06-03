package com.conciergerie.repository;

import com.conciergerie.entity.Ressource;
import com.conciergerie.entity.TypeRessource;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RessourceRepository extends JpaRepository<Ressource, Long> {
    List<Ressource> findByTypeRessource(TypeRessource typeRessource);
    List<Ressource> findByActifTrue();
}
