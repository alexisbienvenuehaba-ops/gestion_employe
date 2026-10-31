package com.gestion_employe.backend.Mapper;

import com.gestion_employe.backend.dto.Request.PaiementClientRequestDto;
import com.gestion_employe.backend.dto.Response.PaiementClientResponseDto;
import com.gestion_employe.backend.entity.FactureEntity;
import com.gestion_employe.backend.entity.PaiementClientEntity;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class PaiementClientMapper {

    public PaiementClientEntity toEntity(PaiementClientRequestDto dto, FactureEntity facture) {
        PaiementClientEntity entity = new PaiementClientEntity();
        entity.setFactureEntity(facture);
        entity.setMontant(dto.getMontant());
        entity.setModePaiement(dto.getModePaiement());
        entity.setDatePaiement(dto.getDatePaiement() != null ? dto.getDatePaiement() : LocalDate.now());
        entity.setReference(dto.getReference());
        entity.setObservation(dto.getObservation());
        return entity;
    }

    public PaiementClientResponseDto toResponseDto(PaiementClientEntity entity) {
        PaiementClientResponseDto dto = new PaiementClientResponseDto();
        dto.setIdPaiementClient(entity.getIdPaiementClient());
        dto.setDatePaiement(entity.getDatePaiement());
        dto.setMontant(entity.getMontant());
        dto.setModePaiement(entity.getModePaiement());
        dto.setReference(entity.getReference());
        dto.setStatut(entity.getStatut());
        dto.setObservation(entity.getObservation());
        dto.setDateValidation(entity.getDateValidation());
        dto.setDateAnnulation(entity.getDateAnnulation());
        dto.setMotifAnnulation(entity.getMotifAnnulation());
        dto.setIdFacture(entity.getFactureEntity().getIdFacture());
        dto.setNumeroFacture(entity.getFactureEntity().getNumeroFacture());
        if (entity.getResponsableEntity() != null) {
            dto.setIdResponsable(entity.getResponsableEntity().getIdEmploye());
        }
        if (entity.getResponsableAnnulationEntity() != null) {
            dto.setIdResponsableAnnulation(entity.getResponsableAnnulationEntity().getIdEmploye());
        }
        return dto;
    }
}