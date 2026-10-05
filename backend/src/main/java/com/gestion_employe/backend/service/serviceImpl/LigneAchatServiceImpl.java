package com.gestion_employe.backend.service.serviceImpl;

import com.gestion_employe.backend.Mapper.LigneAchatMapper;
import com.gestion_employe.backend.dto.Request.LigneAchatRequestDto;
import com.gestion_employe.backend.dto.Response.LigneAchatResponseDto;
import com.gestion_employe.backend.entity.AchatEntity;
import com.gestion_employe.backend.entity.LigneAchatEntity;
import com.gestion_employe.backend.entity.ProduitEntity;
import com.gestion_employe.backend.repository.AchatRepository;
import com.gestion_employe.backend.repository.LigneAchatRepository;
import com.gestion_employe.backend.repository.ProduitRepository;
import com.gestion_employe.backend.service.LigneAchatService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class LigneAchatServiceImpl implements LigneAchatService {

    private final LigneAchatRepository ligneAchatRepository;
    private final AchatRepository achatRepository;
    private final ProduitRepository produitRepository;
    private final LigneAchatMapper ligneAchatMapper;

    @Override
    public LigneAchatResponseDto add(
            LigneAchatRequestDto dto
    ) {

        AchatEntity achat =
                achatRepository.findById(dto.getIdAchat())
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Achat introuvable"
                                )
                        );

        ProduitEntity produit =
                produitRepository.findById(dto.getIdProduit())
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Produit introuvable"
                                )
                        );

        LigneAchatEntity ligne =
                ligneAchatMapper.toEntity(
                        dto,
                        achat,
                        produit
                );

        LigneAchatEntity saved =
                ligneAchatRepository.save(ligne);

        recalculerTotal(achat);

        return ligneAchatMapper.toResponse(saved);
    }

    @Override
    public LigneAchatResponseDto update(
            Long id,
            LigneAchatRequestDto dto
    ) {

        LigneAchatEntity ligne =
                ligneAchatRepository.findById(id)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Ligne d'achat introuvable"
                                )
                        );

        AchatEntity ancienAchat =
                ligne.getAchatEntity();

        AchatEntity nouvelAchat =
                achatRepository.findById(dto.getIdAchat())
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Achat introuvable"
                                )
                        );

        ProduitEntity produit =
                produitRepository.findById(dto.getIdProduit())
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Produit introuvable"
                                )
                        );

        ligne.setQuantite(dto.getQuantite());
        ligne.setPrixUnitaire(dto.getPrixUnitaire());
        ligne.setAchatEntity(nouvelAchat);
        ligne.setProduitEntity(produit);

        LigneAchatEntity updated =
                ligneAchatRepository.save(ligne);

        /*
         * Si la ligne a changé d'achat,
         * on recalcule l'ancien achat également.
         */
        if (!ancienAchat.getIdAchat()
                .equals(nouvelAchat.getIdAchat())) {

            recalculerTotal(ancienAchat);
        }

        recalculerTotal(nouvelAchat);

        return ligneAchatMapper.toResponse(updated);
    }

    @Override
    @Transactional(readOnly = true)
    public LigneAchatResponseDto getById(
            Long id
    ) {

        LigneAchatEntity ligne =
                ligneAchatRepository.findById(id)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Ligne d'achat introuvable"
                                )
                        );

        return ligneAchatMapper.toResponse(ligne);
    }

    @Override
    @Transactional(readOnly = true)
    public List<LigneAchatResponseDto> getAll() {

        return ligneAchatRepository.findAll()
                .stream()
                .map(ligneAchatMapper::toResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<LigneAchatResponseDto> getByAchat(
            Long idAchat
    ) {

        return ligneAchatRepository
                .findAllByAchatEntityIdAchat(idAchat)
                .stream()
                .map(ligneAchatMapper::toResponse)
                .toList();
    }

    @Override
    public void delete(Long id) {

        LigneAchatEntity ligne =
                ligneAchatRepository.findById(id)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Ligne d'achat introuvable"
                                )
                        );

        AchatEntity achat =
                ligne.getAchatEntity();

        ligneAchatRepository.delete(ligne);

        ligneAchatRepository.flush();

        recalculerTotal(achat);
    }

    /**
     * Recalcule le total de l'achat
     * à partir de toutes ses lignes.
     */
    private void recalculerTotal(
            AchatEntity achat
    ) {

        List<LigneAchatEntity> lignes =
                ligneAchatRepository
                        .findAllByAchatEntityIdAchat(
                                achat.getIdAchat()
                        );

        BigDecimal total =
                BigDecimal.ZERO;

        for (LigneAchatEntity ligne : lignes) {

            BigDecimal sousTotal =
                    ligne.getPrixUnitaire()
                            .multiply(
                                    BigDecimal.valueOf(
                                            ligne.getQuantite()
                                    )
                            );

            total = total.add(sousTotal);
        }

        achat.setTotal(total);

        BigDecimal montantPaye =
                achat.getMontantPaye() != null
                        ? achat.getMontantPaye()
                        : BigDecimal.ZERO;

        BigDecimal resteAPayer =
                total.subtract(montantPaye);

        achat.setResteAPayer(resteAPayer);

        achatRepository.save(achat);
    }
}

