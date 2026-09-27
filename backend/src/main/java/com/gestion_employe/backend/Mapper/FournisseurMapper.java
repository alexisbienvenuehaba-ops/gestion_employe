package com.gestion_employe.backend.Mapper;

import com.gestion_employe.backend.dto.Request.FournisseurRequestDto;
import com.gestion_employe.backend.dto.Response.FournisseurResponseDto;
import com.gestion_employe.backend.entity.FournisseurEntity;
import org.springframework.stereotype.Component;

@Component
public class FournisseurMapper {
    public FournisseurEntity toEntity(FournisseurRequestDto request) {
        FournisseurEntity fournisseur = new FournisseurEntity();
        fournisseur.setNom(request.getNom());
        fournisseur.setPrenom(request.getPrenom());
        fournisseur.setTelephone(request.getTelephone());
        return fournisseur;
    }

    public FournisseurResponseDto toResponse(FournisseurEntity entity) {
        return new FournisseurResponseDto(
                entity.getIdFournisseur(),
                entity.getNom(),
                entity.getPrenom(),
                entity.getTelephone()
        );
    }
}