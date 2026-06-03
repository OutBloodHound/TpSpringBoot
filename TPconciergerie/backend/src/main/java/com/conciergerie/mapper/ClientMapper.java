package com.conciergerie.mapper;

import com.conciergerie.dto.ClientDTO;
import com.conciergerie.entity.Client;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface ClientMapper {
    ClientDTO toDto(Client entity);

    @Mapping(target = "utilisateur", ignore = true)
    Client toEntity(ClientDTO dto);
}
