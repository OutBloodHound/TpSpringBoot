package com.conciergerie.repository;

import com.conciergerie.entity.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface ClientRepository extends JpaRepository<Client, Long>{
    Optional<Client> findByUtilisateurId(Long utilisateurId);
}
