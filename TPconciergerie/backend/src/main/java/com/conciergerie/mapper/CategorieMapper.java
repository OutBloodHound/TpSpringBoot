package com.conciergerie.mapper;

import com.conciergerie.dto.CategorieDTO;
import com.conciergerie.entity.Categorie;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface CategorieMapper {
    CategorieDTO toDto(Categorie entity);

    @Mapping(target = "demandes", ignore = true)
    Categorie toEntity(CategorieDTO Dto);
}
