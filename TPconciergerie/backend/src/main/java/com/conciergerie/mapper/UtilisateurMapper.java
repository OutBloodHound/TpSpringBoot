package com.conciergerie.mapper;


import com.conciergerie.dto.UtilisateurDTO;
import com.conciergerie.entity.Utilisateur;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface UtilisateurMapper {
    UtilisateurDTO toDto(Utilisateur entity);

    @Mapping(target = "motDePasse", ignore = true)
    @Mapping(target = "creeLe", ignore = true)
    @Mapping(target = "clients", ignore = true)
    @Mapping(target = "prestataires", ignore = true)
    @Mapping(target = "commentaires", ignore = true)
    @Mapping(target = "notifications", ignore = true)
    Utilisateur toEntity(UtilisateurDTO dto);
}
