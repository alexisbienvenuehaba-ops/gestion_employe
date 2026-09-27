package com.gestion_employe.backend.Mapper;

import com.gestion_employe.backend.dto.Request.ProduitRequestDto;
import com.gestion_employe.backend.dto.Response.ProduitResponseDto;
import com.gestion_employe.backend.entity.FournisseurEntity;
import com.gestion_employe.backend.entity.ProduitEntity;
import com.gestion_employe.backend.repository.FournisseurRepository;
import org.springframework.stereotype.Component;

@Component
public class ProduitMapper {

    private final FournisseurRepository fournisseurRepository;

    public ProduitMapper(FournisseurRepository fournisseurRepository) {
        this.fournisseurRepository = fournisseurRepository;
    }

    public ProduitEntity toEntity(ProduitRequestDto request) {
        ProduitEntity produit = new ProduitEntity();
        produit.setNom(request.getNom());
        produit.setQuantiteStock(request.getQuantiteStock());
        produit.setPrix(request.getPrix());

        FournisseurEntity fournisseur = fournisseurRepository
                .findByIdFournisseurAndDeletedFalse(request.getIdFournisseur())
                .orElseThrow(() -> new RuntimeException(
                        "Fournisseur introuvable avec l'id " + request.getIdFournisseur()));
        produit.setFournisseur(fournisseur);

        return produit;
    }

    public ProduitResponseDto toResponse(ProduitEntity entity) {
        return new ProduitResponseDto(
                entity.getIdProduit(),
                entity.getNom(),
                entity.getQuantiteStock(),
                entity.getPrix(),
                entity.getFournisseur() != null ? entity.getFournisseur().getIdFournisseur() : null,
                entity.getFournisseur() != null ? entity.getFournisseur().getNom() : null
        );
    }
}