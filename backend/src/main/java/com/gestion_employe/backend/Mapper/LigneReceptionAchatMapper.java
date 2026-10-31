package com.gestion_employe.backend.Mapper;

import com.gestion_employe.backend.dto.Request.LigneReceptionAchatRequestDto;
import com.gestion_employe.backend.dto.Response.LigneReceptionAchatResponseDto;
import com.gestion_employe.backend.entity.LigneAchatEntity;
import com.gestion_employe.backend.entity.LigneReceptionAchatEntity;
import com.gestion_employe.backend.entity.ReceptionAchatEntity;
import com.gestion_employe.backend.Enum.DecisionRefus;
import org.springframework.stereotype.Component;

@Component
public class LigneReceptionAchatMapper {

    public LigneReceptionAchatEntity toEntity(LigneReceptionAchatRequestDto dto,
                                              ReceptionAchatEntity reception,
                                              LigneAchatEntity ligneAchat) {
        LigneReceptionAchatEntity entity = new LigneReceptionAchatEntity();
        entity.setReceptionAchatEntity(reception);
        entity.setLigneAchatEntity(ligneAchat);
        appliquerQuantites(entity, dto);
        return entity;
    }

    /** Utilisé à l'ajout et à la modification : recalcule livrée, motif et décision. */
    public void appliquerQuantites(LigneReceptionAchatEntity entity, LigneReceptionAchatRequestDto dto) {
        entity.setQuantiteAcceptee(dto.getQuantiteAcceptee());
        entity.setQuantiteRefusee(dto.getQuantiteRefusee());
        entity.setQuantiteLivree(dto.getQuantiteAcceptee() + dto.getQuantiteRefusee());
        if (dto.getQuantiteRefusee() > 0) {
            entity.setMotifRefus(dto.getMotifRefus());
            entity.setDecisionRefus(DecisionRefus.EN_ATTENTE);
        } else {
            entity.setMotifRefus(null);
            entity.setDecisionRefus(null);
        }
    }

    public LigneReceptionAchatResponseDto toResponseDto(LigneReceptionAchatEntity entity) {
        LigneReceptionAchatResponseDto dto = new LigneReceptionAchatResponseDto();
        dto.setIdLigneReception(entity.getIdLigneReception());
        dto.setIdReceptionAchat(entity.getReceptionAchatEntity().getIdReceptionAchat());
        dto.setIdLigneAchat(entity.getLigneAchatEntity().getIdLigneAchat());
        dto.setIdProduit(entity.getLigneAchatEntity().getProduitEntity().getIdProduit());
        dto.setNomProduit(entity.getLigneAchatEntity().getProduitEntity().getNom());
        dto.setQuantiteCommandee(entity.getLigneAchatEntity().getQuantite());
        dto.setQuantiteLivree(entity.getQuantiteLivree());
        dto.setQuantiteAcceptee(entity.getQuantiteAcceptee());
        dto.setQuantiteRefusee(entity.getQuantiteRefusee());
        dto.setMotifRefus(entity.getMotifRefus());
        dto.setDecisionRefus(entity.getDecisionRefus());
        return dto;
    }
}