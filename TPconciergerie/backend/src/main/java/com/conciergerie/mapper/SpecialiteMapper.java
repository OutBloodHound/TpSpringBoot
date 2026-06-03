package com.conciergerie.mapper;

import com.conciergerie.dto.SpecialiteDTO;
import com.conciergerie.entity.Specialite;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface SpecialiteMapper {
    SpecialiteDTO toDto(Specialite entity);

    @Mapping(target = "prestataires", ignore = true)
    Specialite toEntity(SpecialiteDTO Dto);
}
