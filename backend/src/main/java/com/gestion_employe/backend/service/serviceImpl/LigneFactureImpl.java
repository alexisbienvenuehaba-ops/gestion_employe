package com.gestion_employe.backend.service.serviceImpl;

import com.gestion_employe.backend.Mapper.LigneFactureMapper;
import com.gestion_employe.backend.dto.Request.LigneFactureRequestDto;
import com.gestion_employe.backend.dto.Response.LigneFactureResponseDto;
import com.gestion_employe.backend.entity.FactureEntity;
import com.gestion_employe.backend.entity.LigneFactureEntity;
import com.gestion_employe.backend.entity.ProduitEntity;
import com.gestion_employe.backend.repository.FactureRepository;
import com.gestion_employe.backend.repository.LigneFactureRepository;
import com.gestion_employe.backend.repository.ProduitRepository;
import com.gestion_employe.backend.service.LigneFactureService;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class LigneFactureImpl implements LigneFactureService {
    private final LigneFactureRepository ligneFactureRepository;
    private final FactureRepository factureRepository;
    private final ProduitRepository produitRepository;
    private final LigneFactureMapper ligneFactureMapper;

    public LigneFactureImpl(LigneFactureRepository ligneFactureRepository, FactureRepository factureRepository, ProduitRepository produitRepository, LigneFactureMapper ligneFactureMapper) {
        this.ligneFactureRepository = ligneFactureRepository;
        this.factureRepository = factureRepository;
        this.produitRepository = produitRepository;

        this.ligneFactureMapper = ligneFactureMapper;
    }

    @Override
    public List<LigneFactureResponseDto> getAll() {
        return ligneFactureRepository.findAllByDeletedFalse()
                .stream()
                .map(ligneFactureMapper::toResponse)
                .toList();
    }

    @Override
    public LigneFactureResponseDto getById(Long id) {
        LigneFactureEntity ligneFactureEntity = ligneFactureRepository.findByIdLigneFactureAndDeletedFalse(id)
                .orElseThrow(() -> new RuntimeException("Facture introuvable avec l'id " + id));
        return ligneFactureMapper.toResponse(ligneFactureEntity);
    }

    @Override
    public LigneFactureResponseDto create(LigneFactureRequestDto request) {
        LigneFactureEntity ligneFactureEntity = ligneFactureMapper.toEntity(request);
        LigneFactureEntity saved = ligneFactureRepository.save(ligneFactureEntity);
        return ligneFactureMapper.toResponse(saved);
    }

    @Override
    public LigneFactureResponseDto update(
            Long id,
            LigneFactureRequestDto dto) {

        LigneFactureEntity entity =
                ligneFactureRepository
                        .findByIdLigneFactureAndDeletedFalse(id)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Ligne de facture introuvable avec l'id " + id));

        FactureEntity facture =
                factureRepository
                        .findByIdFactureAndDeletedFalse(dto.getIdFacture())
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Facture introuvable avec l'id " + dto.getIdFacture()));

        ProduitEntity produitEntity =
                produitRepository
                        .findByIdProduitAndDeletedFalse(dto.getIdProduit())
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Produit introuvable avec l'id " + dto.getIdProduit()));

        entity.setFactureEntity(facture);
        entity.setProduitEntity(produitEntity);
        entity.setQuantite(dto.getQuantite());
        entity.setPrixUnitaire(dto.getPrixUnitaire());

        LigneFactureEntity updated =
                ligneFactureRepository.save(entity);

        return ligneFactureMapper.toResponse(updated);
    }

    @Override
    public void delete(Long id) {
        LigneFactureEntity ligneFactureEntity = ligneFactureRepository.findByIdLigneFactureAndDeletedFalse(id)
                .orElseThrow(() -> new RuntimeException("Ligne de facture introuvable avec l'id " + id));
        ligneFactureEntity.setDeleted(true);
        ligneFactureRepository.save(ligneFactureEntity);
    }
}
