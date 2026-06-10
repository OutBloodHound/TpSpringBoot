package com.conciergerie.mapper;

import com.conciergerie.dto.NotificationDTO;
import com.conciergerie.entity.Notification;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface NotificationMapper {
    @Mapping(target = "utilisateurId", source = "utilisateur.id")
    NotificationDTO toDto(Notification entity);

    @Mapping(target = "utilisateur", ignore = true)
    Notification toEntity(NotificationDTO dto);
}
