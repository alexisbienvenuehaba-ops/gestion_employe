package com.gestion_employe.backend.Mapper;

import com.gestion_employe.backend.dto.Response.MouvementStockResponseDto;
import com.gestion_employe.backend.entity.MouvementStockEntity;
import org.springframework.stereotype.Component;

@Component
public class MouvementStockMapper {

    public MouvementStockResponseDto toResponseDto(MouvementStockEntity entity) {
        MouvementStockResponseDto dto = new MouvementStockResponseDto();
        dto.setIdMouvementStock(entity.getIdMouvementStock());
        dto.setIdProduit(entity.getProduitEntity().getIdProduit());
        dto.setNomProduit(entity.getProduitEntity().getNom());
        dto.setTypeMouvement(entity.getTypeMouvement());
        dto.setQuantite(entity.getQuantite());
        dto.setDateMouvement(entity.getDateMouvement());
        dto.setOrigine(entity.getOrigine());
        if (entity.getLigneReceptionAchatEntity() != null) {
            dto.setIdLigneReception(entity.getLigneReceptionAchatEntity().getIdLigneReception());
        }
        if (entity.getResponsableEntity() != null) {
            dto.setIdResponsable(entity.getResponsableEntity().getIdEmploye());
        }
        dto.setMotif(entity.getMotif());
        return dto;
    }
}