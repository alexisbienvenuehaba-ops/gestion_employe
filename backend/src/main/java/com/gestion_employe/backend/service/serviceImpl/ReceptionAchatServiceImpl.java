package com.gestion_employe.backend.service.serviceImpl;

import com.gestion_employe.backend.Enum.DecisionRefus;
import com.gestion_employe.backend.Enum.OrigineMouvement;
import com.gestion_employe.backend.Enum.StatutReception;
import com.gestion_employe.backend.Enum.StatutReceptionAchat;
import com.gestion_employe.backend.Enum.TypeAction;
import com.gestion_employe.backend.Enum.TypeEntiteHistorique;
import com.gestion_employe.backend.Enum.TypeMouvement;
import com.gestion_employe.backend.Mapper.LigneReceptionAchatMapper;
import com.gestion_employe.backend.Mapper.ReceptionAchatMapper;
import com.gestion_employe.backend.dto.Request.DecisionRefusRequestDto;
import com.gestion_employe.backend.dto.Request.ReceptionAchatRequestDto;
import com.gestion_employe.backend.dto.Request.ValiderReceptionRequestDto;
import com.gestion_employe.backend.dto.Response.LigneReceptionAchatResponseDto;
import com.gestion_employe.backend.dto.Response.ReceptionAchatResponseDto;
import com.gestion_employe.backend.entity.AchatEntity;
import com.gestion_employe.backend.entity.EmployeEntity;
import com.gestion_employe.backend.entity.LigneAchatEntity;
import com.gestion_employe.backend.entity.LigneReceptionAchatEntity;
import com.gestion_employe.backend.entity.ReceptionAchatEntity;
import com.gestion_employe.backend.repository.AchatRepository;
import com.gestion_employe.backend.repository.LigneAchatRepository;
import com.gestion_employe.backend.repository.LigneReceptionAchatRepository;
import com.gestion_employe.backend.repository.ReceptionAchatRepository;
import com.gestion_employe.backend.service.HistoriqueActionService;
import com.gestion_employe.backend.service.MouvementStockService;
import com.gestion_employe.backend.service.ReceptionAchatService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.LockModeType;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReceptionAchatServiceImpl implements ReceptionAchatService {

    private final ReceptionAchatRepository receptionAchatRepository;
    private final LigneReceptionAchatRepository ligneReceptionRepository;
    private final AchatRepository achatRepository;
    private final LigneAchatRepository ligneAchatRepository;
    private final MouvementStockService mouvementStockService;
    private final HistoriqueActionService historiqueActionService;
    private final ReceptionAchatMapper mapper;
    private final LigneReceptionAchatMapper ligneMapper;

    @PersistenceContext
    private EntityManager entityManager;

    // ------------------------------------------------------------------ Partie 1

    @Override
    @Transactional
    public ReceptionAchatResponseDto creer(ReceptionAchatRequestDto dto) {
        AchatEntity achat = trouverAchat(dto.getIdAchat());
        ReceptionAchatEntity reception = mapper.toEntity(dto, achat);
        return mapper.toResponseDto(receptionAchatRepository.save(reception));
    }

    @Override
    @Transactional(readOnly = true)
    public ReceptionAchatResponseDto trouverParId(Long id) {
        return mapper.toResponseDto(trouverReception(id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<ReceptionAchatResponseDto> lister() {
        return receptionAchatRepository.findAll().stream().map(mapper::toResponseDto).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<ReceptionAchatResponseDto> listerParAchat(Long idAchat) {
        AchatEntity achat = trouverAchat(idAchat);
        return receptionAchatRepository.findByAchatEntityOrderByDateReceptionDesc(achat)
                .stream().map(mapper::toResponseDto).toList();
    }

    @Override
    @Transactional
    public ReceptionAchatResponseDto annuler(Long id) {
        ReceptionAchatEntity reception = trouverReception(id);
        if (reception.getStatut() != StatutReception.BROUILLON) {
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "Seule une réception en brouillon peut être annulée.");
        }
        reception.setStatut(StatutReception.ANNULEE);
        return mapper.toResponseDto(receptionAchatRepository.save(reception));
    }

    // ------------------------------------------------------------------ Partie 4

    @Override
    @Transactional
    public ReceptionAchatResponseDto valider(Long id, ValiderReceptionRequestDto dto) {
        ReceptionAchatEntity reception = trouverReception(id);

        // Verrou sur l'achat : une seule validation à la fois pour un même achat.
        AchatEntity achat = verrouillerAchat(reception.getAchatEntity().getIdAchat());
        entityManager.refresh(reception);

        if (reception.getStatut() != StatutReception.BROUILLON) {
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "Seule une réception en brouillon peut être validée.");
        }

        List<LigneReceptionAchatEntity> lignes = ligneReceptionRepository.findByReceptionAchatEntity(reception);
        if (lignes.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Impossible de valider une réception sans ligne.");
        }

        EmployeEntity responsable = trouverResponsable(dto.getIdResponsable());

        // Les quantités sont re-contrôlées : d'autres réceptions ont pu être validées depuis la saisie.
        for (LigneReceptionAchatEntity ligne : lignes) {
            verifierResteARecevoir(ligne.getLigneAchatEntity(), ligne.getQuantiteAcceptee());
        }

        reception.setStatut(StatutReception.VALIDEE);
        reception.setDateValidation(LocalDateTime.now());
        reception.setResponsableEntity(responsable);
        receptionAchatRepository.saveAndFlush(reception);

        // Seules les quantités acceptées entrent en stock.
        for (LigneReceptionAchatEntity ligne : lignes) {
            if (ligne.getQuantiteAcceptee() > 0) {
                mouvementStockService.enregistrer(
                        ligne.getLigneAchatEntity().getProduitEntity().getIdProduit(),
                        TypeMouvement.ENTREE,
                        ligne.getQuantiteAcceptee(),
                        OrigineMouvement.RECEPTION_ACHAT,
                        ligne,
                        responsable,
                        "Réception n°" + reception.getIdReceptionAchat());
            }
        }

        recalculerStatutAchat(achat);

        historiqueActionService.enregistrer(
                TypeAction.RECEPTION_VALIDEE, TypeEntiteHistorique.RECEPTION_ACHAT,
                reception.getIdReceptionAchat(), responsable,
                "Réception n°" + reception.getIdReceptionAchat() + " validée pour l'achat n°" + achat.getIdAchat());

        return mapper.toResponseDto(reception);
    }

    @Override
    @Transactional
    public LigneReceptionAchatResponseDto deciderRefus(Long idLigneReception, DecisionRefusRequestDto dto) {
        LigneReceptionAchatEntity ligne = ligneReceptionRepository.findById(idLigneReception).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Ligne de réception introuvable : " + idLigneReception));

        AchatEntity achat = verrouillerAchat(ligne.getReceptionAchatEntity().getAchatEntity().getIdAchat());
        entityManager.refresh(ligne);

        if (ligne.getReceptionAchatEntity().getStatut() != StatutReception.VALIDEE) {
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "La décision n'est possible que sur une réception validée.");
        }
        if (ligne.getDecisionRefus() != DecisionRefus.EN_ATTENTE) {
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "Aucune décision en attente sur cette ligne.");
        }
        if (dto.getDecision() == DecisionRefus.EN_ATTENTE) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "La décision doit être RETOUR_FOURNISSEUR ou ACCEPTEE_EXCEPTIONNELLE.");
        }

        EmployeEntity responsable = trouverResponsable(dto.getIdResponsable());

        if (dto.getDecision() == DecisionRefus.ACCEPTEE_EXCEPTIONNELLE) {
            if (dto.getMotif() == null || dto.getMotif().isBlank()) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                        "Le motif est obligatoire pour une acceptation exceptionnelle.");
            }
            verifierResteARecevoir(ligne.getLigneAchatEntity(), ligne.getQuantiteRefusee());
            mouvementStockService.enregistrer(
                    ligne.getLigneAchatEntity().getProduitEntity().getIdProduit(),
                    TypeMouvement.ENTREE,
                    ligne.getQuantiteRefusee(),
                    OrigineMouvement.ACCEPTATION_EXCEPTIONNELLE,
                    ligne,
                    responsable,
                    dto.getMotif());
        }

        ligne.setDecisionRefus(dto.getDecision());
        ligneReceptionRepository.saveAndFlush(ligne);

        recalculerStatutAchat(achat);

        historiqueActionService.enregistrer(
                TypeAction.REFUS_DECIDE, TypeEntiteHistorique.LIGNE_RECEPTION_ACHAT,
                ligne.getIdLigneReception(), responsable,
                "Décision " + dto.getDecision() + " pour " + ligne.getQuantiteRefusee()
                        + " unité(s) refusée(s) de " + ligne.getLigneAchatEntity().getProduitEntity().getNom());

        return ligneMapper.toResponseDto(ligne);
    }

    // ------------------------------------------------------------------ Outils

    /** Quantité déjà reçue pour une ligne d'achat : acceptées + refusées finalement acceptées. */
    private long quantiteRecue(LigneAchatEntity ligneAchat) {
        return ligneReceptionRepository.sommeAcceptees(ligneAchat, StatutReception.VALIDEE)
                + ligneReceptionRepository.sommeAccepteesExceptionnellement(
                ligneAchat, StatutReception.VALIDEE, DecisionRefus.ACCEPTEE_EXCEPTIONNELLE);
    }

    private void verifierResteARecevoir(LigneAchatEntity ligneAchat, int quantite) {
        long restant = ligneAchat.getQuantite() - quantiteRecue(ligneAchat);
        if (quantite > restant) {
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "Quantité (" + quantite + ") supérieure au reste à recevoir (" + restant + ").");
        }
    }

    /** Le statut de l'achat est toujours calculé, jamais saisi. */
    private void recalculerStatutAchat(AchatEntity achat) {
        List<LigneAchatEntity> lignesAchat = ligneAchatRepository.findAllByAchatEntityIdAchat(achat.getIdAchat());

        long totalCommande = 0;
        long totalRecu = 0;
        boolean toutesLignesEntamees = true;
        for (LigneAchatEntity ligneAchat : lignesAchat) {
            long recu = quantiteRecue(ligneAchat);
            totalCommande += ligneAchat.getQuantite();
            totalRecu += recu;
            if (recu <= 0) {
                toutesLignesEntamees = false;
            }
        }

        boolean decisionEnAttente = ligneReceptionRepository.existeRefusEnAttente(
                achat, StatutReception.VALIDEE, DecisionRefus.EN_ATTENTE);

        StatutReceptionAchat statut;
        if (totalRecu == 0) {
            statut = StatutReceptionAchat.EN_ATTENTE_RECEPTION;
        } else if (totalRecu >= totalCommande && !decisionEnAttente) {
            statut = StatutReceptionAchat.RECEPTIONNE;
        } else if (toutesLignesEntamees) {
            statut = StatutReceptionAchat.EN_ATTENTE_RELIQUAT;
        } else {
            statut = StatutReceptionAchat.PARTIELLEMENT_RECEPTIONNE;
        }

        achat.setStatutReception(statut);
        achatRepository.save(achat);
    }

    private AchatEntity verrouillerAchat(Long idAchat) {
        AchatEntity achat = entityManager.find(AchatEntity.class, idAchat, LockModeType.PESSIMISTIC_WRITE);
        if (achat == null || achat.isDeleted()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Achat introuvable : " + idAchat);
        }
        return achat;
    }

    private EmployeEntity trouverResponsable(Long idResponsable) {
        EmployeEntity responsable = entityManager.find(EmployeEntity.class, idResponsable);
        if (responsable == null || responsable.isDeleted()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    "Responsable introuvable : " + idResponsable);
        }
        return responsable;
    }

    private AchatEntity trouverAchat(Long idAchat) {
        AchatEntity achat = achatRepository.findById(idAchat).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Achat introuvable : " + idAchat));
        if (achat.isDeleted()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Achat introuvable : " + idAchat);
        }
        return achat;
    }

    private ReceptionAchatEntity trouverReception(Long id) {
        return receptionAchatRepository.findById(id).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Réception introuvable : " + id));
    }
}