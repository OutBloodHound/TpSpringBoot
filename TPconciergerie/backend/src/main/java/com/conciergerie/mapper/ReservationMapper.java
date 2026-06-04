package com.conciergerie.mapper;

import com.conciergerie.dto.ReservationDTO;
import com.conciergerie.entity.Reservation;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface ReservationMapper {
    @Mapping(target = "clientId", source = "client.id")
    @Mapping(target = "ressourceId", source = "ressource.id")
    ReservationDTO toDto(Reservation entity);

    @Mapping(target = "client", ignore = true)
    @Mapping(target = "ressource", ignore = true)
    Reservation toEntity(ReservationDTO dto);
}