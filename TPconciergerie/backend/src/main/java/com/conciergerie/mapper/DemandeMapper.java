package com.conciergerie.mapper;

import com.conciergerie.dto.DemandeDTO;
import com.conciergerie.entity.Demande;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface DemandeMapper {
    DemandeDTO toDto(Demande entity);

    @Mapping(target = "client", ignore = true)
    @Mapping(target = "prestataire", ignore = true)
    @Mapping(target = "categorie", ignore = true)
    @Mapping(target = "commentaires", ignore = true)
    @Mapping(target = "notations", ignore = true)
    @Mapping(target = "pieceJointes", ignore = true)
    Demande toEntity(DemandeDTO dto);
}
