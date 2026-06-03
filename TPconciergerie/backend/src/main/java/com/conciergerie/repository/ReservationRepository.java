package com.conciergerie.repository;

import com.conciergerie.entity.Reservation;
import com.conciergerie.entity.StatutReservation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {
    List<Reservation> findByClientId(Long clientId);
    List<Reservation> findByRessourceId(Long ressourceId);
    List<Reservation> findByStatut(StatutReservation statut);
}
