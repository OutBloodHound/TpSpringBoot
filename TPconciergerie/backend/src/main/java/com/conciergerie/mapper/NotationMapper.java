package com.conciergerie.mapper;

import com.conciergerie.dto.NotationDTO;
import com.conciergerie.entity.Notation;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface NotationMapper {
    @Mapping(target = "demandeId", source = "demande.id")
    @Mapping(target = "prestataireId", source = "prestataire.id")
    NotationDTO toDto(Notation entity);

    @Mapping(target = "demande", ignore = true)
    @Mapping(target = "prestataire", ignore = true)
    Notation toEntity(NotationDTO dto);
}
