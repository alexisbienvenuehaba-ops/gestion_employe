package com.gestion_employe.backend.Mapper;

import com.gestion_employe.backend.dto.Request.LigneFactureRequestDto;
import com.gestion_employe.backend.dto.Response.LigneFactureResponseDto;
import com.gestion_employe.backend.entity.FactureEntity;
import com.gestion_employe.backend.entity.LigneFactureEntity;
import com.gestion_employe.backend.entity.ProduitEntity;
import com.gestion_employe.backend.repository.FactureRepository;
import com.gestion_employe.backend.repository.ProduitRepository;
import org.springframework.stereotype.Component;

@Component
public class LigneFactureMapper {

    private final FactureRepository factureRepository;
    private final ProduitRepository produitRepository;

    public LigneFactureMapper(
            FactureRepository factureRepository,
            ProduitRepository produitRepository) {

        this.factureRepository = factureRepository;
        this.produitRepository = produitRepository;
    }

    public LigneFactureEntity toEntity(
            LigneFactureRequestDto request) {

        LigneFactureEntity entity = new LigneFactureEntity();

        entity.setQuantite(request.getQuantite());
        entity.setPrixUnitaire(request.getPrixUnitaire());

        FactureEntity facture = factureRepository
                .findByIdFactureAndDeletedFalse(request.getIdFacture())
                .orElseThrow(() ->
                        new RuntimeException(
                                "Facture introuvable avec l'id "
                                        + request.getIdFacture()));

        entity.setFactureEntity(facture);

        ProduitEntity produit = produitRepository
                .findByIdProduitAndDeletedFalse(request.getIdProduit())
                .orElseThrow(() ->
                        new RuntimeException(
                                "Produit introuvable avec l'id "
                                        + request.getIdProduit()));

        entity.setProduitEntity(produit);

        return entity;
    }

    public LigneFactureResponseDto toResponse(
            LigneFactureEntity entity) {

        return new LigneFactureResponseDto(

                entity.getIdLigneFacture(),

                entity.getQuantite(),

                entity.getPrixUnitaire(),

                entity.getFactureEntity() != null
                        ? entity.getFactureEntity().getIdFacture()
                        : null,

                entity.getFactureEntity() != null
                        ? entity.getFactureEntity().getNumeroFacture()
                        : null,

                entity.getFactureEntity() != null
                        && entity.getFactureEntity().getClientEntity() != null
                        ? entity.getFactureEntity()
                        .getClientEntity()
                        .getNom()
                        : null,

                entity.getProduitEntity() != null
                        ? entity.getProduitEntity().getIdProduit()
                        : null,

                entity.getProduitEntity() != null
                        ? entity.getProduitEntity().getNom()
                        : null
        );
    }
}