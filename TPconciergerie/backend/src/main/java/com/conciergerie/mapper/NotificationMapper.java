package com.conciergerie.mapper;

import com.conciergerie.dto.NotificationDTO;
import com.conciergerie.entity.Notification;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface NotificationMapper {
    NotificationDTO toDto(Notification entity);

    @Mapping(target = "utilisateur", ignore = true)
    Notification toEntity(NotificationDTO dto);
}
