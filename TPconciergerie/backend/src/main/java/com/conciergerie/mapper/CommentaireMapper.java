package com.conciergerie.mapper;

import com.conciergerie.dto.CommentaireDTO;
import com.conciergerie.entity.Commentaire;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface CommentaireMapper {
    @Mapping(target = "demandeId", source = "demande.id")
    @Mapping(target = "utilisateurId", source = "utilisateur.id")
    CommentaireDTO toDto(Commentaire entity);

    @Mapping(target = "demande", ignore = true)
    @Mapping(target = "utilisateur", ignore = true)
    Commentaire toEntity(CommentaireDTO dto);
}
