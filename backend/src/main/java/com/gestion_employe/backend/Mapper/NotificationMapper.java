package com.gestion_employe.backend.Mapper;

import com.gestion_employe.backend.dto.Response.NotificationResponseDto;
import com.gestion_employe.backend.entity.NotificationEntity;
import org.springframework.stereotype.Component;

@Component
public class NotificationMapper {

    public NotificationResponseDto toResponseDto(NotificationEntity entity) {
        NotificationResponseDto dto = new NotificationResponseDto();
        dto.setIdNotification(entity.getIdNotification());
        dto.setTypeNotification(entity.getTypeNotification());
        dto.setNiveau(entity.getNiveau());
        dto.setTitre(entity.getTitre());
        dto.setMessage(entity.getMessage());
        dto.setDateCreation(entity.getDateCreation());
        dto.setStatut(entity.getStatut());
        dto.setDateLecture(entity.getDateLecture());
        dto.setDateResolution(entity.getDateResolution());
        dto.setTypeEntite(entity.getTypeEntite());
        dto.setIdEntite(entity.getIdEntite());
        return dto;
    }
}