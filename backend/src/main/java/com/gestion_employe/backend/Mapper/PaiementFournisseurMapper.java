package com.gestion_employe.backend.Mapper;

import com.gestion_employe.backend.dto.Request.PaiementFournisseurRequestDto;
import com.gestion_employe.backend.dto.Response.PaiementFournisseurResponseDto;
import com.gestion_employe.backend.entity.AchatEntity;
import com.gestion_employe.backend.entity.PaiementFournisseurEntity;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class PaiementFournisseurMapper {

    public PaiementFournisseurEntity toEntity(PaiementFournisseurRequestDto dto, AchatEntity achat) {
        PaiementFournisseurEntity entity = new PaiementFournisseurEntity();
        entity.setAchatEntity(achat);
        entity.setMontant(dto.getMontant());
        entity.setModePaiement(dto.getModePaiement());
        entity.setDatePaiement(dto.getDatePaiement() != null ? dto.getDatePaiement() : LocalDate.now());
        entity.setReference(dto.getReference());
        entity.setObservation(dto.getObservation());
        return entity;
    }

    public PaiementFournisseurResponseDto toResponseDto(PaiementFournisseurEntity entity) {
        PaiementFournisseurResponseDto dto = new PaiementFournisseurResponseDto();
        dto.setIdPaiementFournisseur(entity.getIdPaiementFournisseur());
        dto.setDatePaiement(entity.getDatePaiement());
        dto.setMontant(entity.getMontant());
        dto.setModePaiement(entity.getModePaiement());
        dto.setReference(entity.getReference());
        dto.setStatut(entity.getStatut());
        dto.setObservation(entity.getObservation());
        dto.setDateValidation(entity.getDateValidation());
        dto.setDateAnnulation(entity.getDateAnnulation());
        dto.setMotifAnnulation(entity.getMotifAnnulation());
        dto.setIdAchat(entity.getAchatEntity().getIdAchat());
        if (entity.getResponsableEntity() != null) {
            dto.setIdResponsable(entity.getResponsableEntity().getIdEmploye());
        }
        if (entity.getResponsableAnnulationEntity() != null) {
            dto.setIdResponsableAnnulation(entity.getResponsableAnnulationEntity().getIdEmploye());
        }
        return dto;
    }
}