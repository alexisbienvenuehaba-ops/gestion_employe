package com.gestion_employe.backend.Mapper;

import com.gestion_employe.backend.dto.Request.ReceptionAchatRequestDto;
import com.gestion_employe.backend.dto.Response.ReceptionAchatResponseDto;
import com.gestion_employe.backend.entity.AchatEntity;
import com.gestion_employe.backend.entity.ReceptionAchatEntity;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class ReceptionAchatMapper {

    public ReceptionAchatEntity toEntity(ReceptionAchatRequestDto dto, AchatEntity achat) {
        ReceptionAchatEntity entity = new ReceptionAchatEntity();
        entity.setAchatEntity(achat);
        entity.setDateReception(dto.getDateReception() != null ? dto.getDateReception() : LocalDate.now());
        entity.setObservation(dto.getObservation());
        return entity;
    }

    public ReceptionAchatResponseDto toResponseDto(ReceptionAchatEntity entity) {
        ReceptionAchatResponseDto dto = new ReceptionAchatResponseDto();
        dto.setIdReceptionAchat(entity.getIdReceptionAchat());
        dto.setDateReception(entity.getDateReception());
        dto.setStatut(entity.getStatut());
        dto.setObservation(entity.getObservation());
        dto.setDateValidation(entity.getDateValidation());
        dto.setIdAchat(entity.getAchatEntity().getIdAchat());
        if (entity.getResponsableEntity() != null) {
            dto.setIdResponsable(entity.getResponsableEntity().getIdEmploye());
        }
        return dto;
    }
}