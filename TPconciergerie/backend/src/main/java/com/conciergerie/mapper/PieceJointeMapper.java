package com.conciergerie.mapper;

import com.conciergerie.dto.PieceJointeDTO;
import com.conciergerie.entity.PieceJointe;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface PieceJointeMapper {
    @Mapping(target = "demandeId", source = "demande.id")
    PieceJointeDTO toDto(PieceJointe entity);

    @Mapping(target = "demande", ignore = true)
    PieceJointe toEntity(PieceJointeDTO dto);
}
