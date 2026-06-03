package com.conciergerie.mapper;

import com.conciergerie.dto.RessourceDTO;
import com.conciergerie.entity.Ressource;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface RessourceMapper {
    RessourceDTO toDto(Ressource entity);

    @Mapping(target = "reservations", ignore = true)
    Ressource toEntity(RessourceDTO Dto);
}
