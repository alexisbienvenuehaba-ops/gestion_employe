package com.gestion_employe.backend.service.serviceImpl;

import com.gestion_employe.backend.Mapper.ProduitMapper;
import com.gestion_employe.backend.dto.Request.ProduitRequestDto;
import com.gestion_employe.backend.dto.Response.ProduitResponseDto;
import com.gestion_employe.backend.entity.FournisseurEntity;
import com.gestion_employe.backend.entity.ProduitEntity;
import com.gestion_employe.backend.repository.FournisseurRepository;
import com.gestion_employe.backend.repository.ProduitRepository;
import com.gestion_employe.backend.service.ProduitService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProduitServiceImpl implements ProduitService {

    private final ProduitRepository produitRepository;
    private final FournisseurRepository fournisseurRepository;
    private final ProduitMapper produitMapper;

    public ProduitServiceImpl(ProduitRepository produitRepository,
                              FournisseurRepository fournisseurRepository,
                              ProduitMapper produitMapper) {
        this.produitRepository = produitRepository;
        this.fournisseurRepository = fournisseurRepository;
        this.produitMapper = produitMapper;
    }

    @Override
    public List<ProduitResponseDto> getAll() {
        return produitRepository.findAllByDeletedFalse()
                .stream()
                .map(produitMapper::toResponse)
                .toList();
    }

    @Override
    public ProduitResponseDto getById(Long id) {
        ProduitEntity produit = produitRepository.findByIdProduitAndDeletedFalse(id)
                .orElseThrow(() -> new RuntimeException("Produit introuvable avec l'id " + id));
        return produitMapper.toResponse(produit);
    }

    @Override
    public ProduitResponseDto create(ProduitRequestDto request) {
        ProduitEntity produit = produitMapper.toEntity(request);
        ProduitEntity saved = produitRepository.save(produit);
        return produitMapper.toResponse(saved);
    }

    @Override
    public ProduitResponseDto update(Long id, ProduitRequestDto request) {
        ProduitEntity produit = produitRepository.findByIdProduitAndDeletedFalse(id)
                .orElseThrow(() -> new RuntimeException("Produit introuvable avec l'id " + id));

        produit.setNom(request.getNom());
        produit.setQuantiteStock(request.getQuantiteStock());
        produit.setPrix(request.getPrix());

        FournisseurEntity fournisseur = fournisseurRepository
                .findByIdFournisseurAndDeletedFalse(request.getIdFournisseur())
                .orElseThrow(() -> new RuntimeException(
                        "Fournisseur introuvable avec l'id " + request.getIdFournisseur()));
        produit.setFournisseur(fournisseur);

        ProduitEntity updated = produitRepository.save(produit);
        return produitMapper.toResponse(updated);
    }

    @Override
    public void delete(Long id) {
        ProduitEntity produit = produitRepository.findByIdProduitAndDeletedFalse(id)
                .orElseThrow(() -> new RuntimeException("Produit introuvable avec l'id " + id));
        produit.setDeleted(true);
        produitRepository.save(produit);
    }
}