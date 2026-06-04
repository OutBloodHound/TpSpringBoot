package com.conciergerie.mapper;

import com.conciergerie.dto.ClientDTO;
import com.conciergerie.entity.Client;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface ClientMapper {
    @Mapping(target = "utilisateurId", source = "utilisateur.id")
    ClientDTO toDto(Client entity);

    @Mapping(target = "utilisateur", ignore = true)
    @Mapping(target = "reservations", ignore = true)
    @Mapping(target = "demandes", ignore = true)
    Client toEntity(ClientDTO dto);
}
