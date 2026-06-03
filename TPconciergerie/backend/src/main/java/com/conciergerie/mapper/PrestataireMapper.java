package com.conciergerie.mapper;

import com.conciergerie.dto.PrestataireDTO;
import com.conciergerie.entity.Prestataire;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface PrestataireMapper {
    PrestataireDTO toDto(Prestataire entity);

    @Mapping(target = "utilisateur", ignore = true)
    @Mapping(target = "specialites", ignore = true)
    @Mapping(target = "notations", ignore = true)
    Prestataire toEntity(PrestataireDTO dto);
}
