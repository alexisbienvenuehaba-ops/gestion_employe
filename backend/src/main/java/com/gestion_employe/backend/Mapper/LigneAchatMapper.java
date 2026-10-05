package com.gestion_employe.backend.Mapper;

import com.gestion_employe.backend.dto.Request.LigneAchatRequestDto;
import com.gestion_employe.backend.dto.Response.LigneAchatResponseDto;
import com.gestion_employe.backend.entity.AchatEntity;
import com.gestion_employe.backend.entity.LigneAchatEntity;
import com.gestion_employe.backend.entity.ProduitEntity;
import org.springframework.stereotype.Component;

@Component
public class LigneAchatMapper {

    public LigneAchatEntity toEntity(
            LigneAchatRequestDto dto,
            AchatEntity achat,
            ProduitEntity produit
    ) {

        LigneAchatEntity ligne = new LigneAchatEntity();

        ligne.setQuantite(dto.getQuantite());
        ligne.setPrixUnitaire(dto.getPrixUnitaire());
        ligne.setAchatEntity(achat);
        ligne.setProduitEntity(produit);

        return ligne;
    }

    public LigneAchatResponseDto toResponse(
            LigneAchatEntity ligne
    ) {

        return new LigneAchatResponseDto(
                ligne.getIdLigneAchat(),
                ligne.getQuantite(),
                ligne.getPrixUnitaire(),
                ligne.getAchatEntity().getIdAchat(),
                ligne.getProduitEntity().getIdProduit(),
                ligne.getProduitEntity().getNom()
        );
    }
}