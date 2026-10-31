package com.gestion_employe.backend.service.serviceImpl;

import com.gestion_employe.backend.dto.Request.LigneReceptionAchatRequestDto;
import com.gestion_employe.backend.dto.Response.LigneReceptionAchatResponseDto;
import com.gestion_employe.backend.entity.LigneAchatEntity;
import com.gestion_employe.backend.entity.LigneReceptionAchatEntity;
import com.gestion_employe.backend.entity.ReceptionAchatEntity;
import com.gestion_employe.backend.Enum.DecisionRefus;
import com.gestion_employe.backend.Enum.StatutReception;
import com.gestion_employe.backend.Mapper.LigneReceptionAchatMapper;
import com.gestion_employe.backend.repository.LigneAchatRepository;
import com.gestion_employe.backend.repository.LigneReceptionAchatRepository;
import com.gestion_employe.backend.repository.ReceptionAchatRepository;
import com.gestion_employe.backend.service.LigneReceptionAchatService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class LigneReceptionAchatServiceImpl implements LigneReceptionAchatService {

    private final LigneReceptionAchatRepository ligneReceptionRepository;
    private final ReceptionAchatRepository receptionRepository;
    private final LigneAchatRepository ligneAchatRepository;
    private final LigneReceptionAchatMapper mapper;

    @Override
    @Transactional
    public LigneReceptionAchatResponseDto ajouter(Long idReception, LigneReceptionAchatRequestDto dto) {
        ReceptionAchatEntity reception = trouverReceptionBrouillon(idReception);

        if (dto.getIdLigneAchat() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "idLigneAchat est obligatoire.");
        }
        LigneAchatEntity ligneAchat = ligneAchatRepository.findById(dto.getIdLigneAchat()).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Ligne d'achat introuvable : " + dto.getIdLigneAchat()));

        if (!ligneAchat.getAchatEntity().getIdAchat().equals(reception.getAchatEntity().getIdAchat())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Cette ligne n'appartient pas à l'achat de la réception.");
        }
        if (ligneReceptionRepository.existsByReceptionAchatEntityAndLigneAchatEntity(reception, ligneAchat)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "Ce produit est déjà dans cette réception. Modifie la ligne existante.");
        }

        controlerQuantites(ligneAchat, dto);
        LigneReceptionAchatEntity ligne = mapper.toEntity(dto, reception, ligneAchat);
        return mapper.toResponseDto(ligneReceptionRepository.save(ligne));
    }

    @Override
    @Transactional
    public LigneReceptionAchatResponseDto modifier(Long idLigneReception, LigneReceptionAchatRequestDto dto) {
        LigneReceptionAchatEntity ligne = trouverLigne(idLigneReception);
        verifierBrouillon(ligne.getReceptionAchatEntity());

        controlerQuantites(ligne.getLigneAchatEntity(), dto);
        mapper.appliquerQuantites(ligne, dto);
        return mapper.toResponseDto(ligneReceptionRepository.save(ligne));
    }

    @Override
    @Transactional
    public void supprimer(Long idLigneReception) {
        LigneReceptionAchatEntity ligne = trouverLigne(idLigneReception);
        verifierBrouillon(ligne.getReceptionAchatEntity());
        ligneReceptionRepository.delete(ligne);
    }

    @Override
    @Transactional(readOnly = true)
    public List<LigneReceptionAchatResponseDto> listerParReception(Long idReception) {
        ReceptionAchatEntity reception = trouverReception(idReception);
        return ligneReceptionRepository.findByReceptionAchatEntity(reception)
                .stream().map(mapper::toResponseDto).toList();
    }

    /** Règles de quantités, contrôlées côté backend. */
    private void controlerQuantites(LigneAchatEntity ligneAchat, LigneReceptionAchatRequestDto dto) {
        int acceptee = dto.getQuantiteAcceptee();
        int refusee = dto.getQuantiteRefusee();

        if (acceptee + refusee <= 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "La quantité livrée doit être supérieure à 0.");
        }
        if (refusee > 0 && (dto.getMotifRefus() == null || dto.getMotifRefus().isBlank())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Le motif de refus est obligatoire quand des produits sont refusés.");
        }

        // Déjà reçu = acceptés (réceptions validées) + refusés finalement acceptés exceptionnellement.
        long dejaRecu = ligneReceptionRepository.sommeAcceptees(ligneAchat, StatutReception.VALIDEE)
                + ligneReceptionRepository.sommeAccepteesExceptionnellement(
                ligneAchat, StatutReception.VALIDEE, DecisionRefus.ACCEPTEE_EXCEPTIONNELLE);
        long restant = ligneAchat.getQuantite() - dejaRecu;

        if (acceptee > restant) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Quantité acceptée (" + acceptee + ") supérieure au reste à recevoir (" + restant + ").");
        }
    }

    private ReceptionAchatEntity trouverReception(Long id) {
        return receptionRepository.findById(id).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Réception introuvable : " + id));
    }

    private ReceptionAchatEntity trouverReceptionBrouillon(Long id) {
        ReceptionAchatEntity reception = trouverReception(id);
        verifierBrouillon(reception);
        return reception;
    }

    private void verifierBrouillon(ReceptionAchatEntity reception) {
        if (reception.getStatut() != StatutReception.BROUILLON) {
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "Les lignes ne se modifient que sur une réception en brouillon.");
        }
    }

    private LigneReceptionAchatEntity trouverLigne(Long id) {
        return ligneReceptionRepository.findById(id).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Ligne de réception introuvable : " + id));
    }
}