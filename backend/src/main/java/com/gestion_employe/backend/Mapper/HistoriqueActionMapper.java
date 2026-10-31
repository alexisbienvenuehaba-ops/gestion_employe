package com.gestion_employe.backend.Mapper;

import com.gestion_employe.backend.dto.Response.HistoriqueActionResponseDto;
import com.gestion_employe.backend.entity.HistoriqueActionEntity;
import org.springframework.stereotype.Component;

@Component
public class HistoriqueActionMapper {

    public HistoriqueActionResponseDto toResponseDto(HistoriqueActionEntity entity) {
        HistoriqueActionResponseDto dto = new HistoriqueActionResponseDto();
        dto.setIdHistoriqueAction(entity.getIdHistoriqueAction());
        dto.setDateAction(entity.getDateAction());
        dto.setTypeAction(entity.getTypeAction());
        dto.setTypeEntite(entity.getTypeEntite());
        dto.setIdEntite(entity.getIdEntite());
        dto.setDescription(entity.getDescription());
        dto.setIdEmploye(entity.getEmployeEntity().getIdEmploye());
        dto.setNomEmploye(entity.getEmployeEntity().getNom());
        dto.setPrenomEmploye(entity.getEmployeEntity().getPrenom());
        return dto;
    }
}