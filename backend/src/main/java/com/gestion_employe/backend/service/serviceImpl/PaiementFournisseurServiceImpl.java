package com.gestion_employe.backend.service.serviceImpl;

import com.gestion_employe.backend.Enum.StatutPaiement;
import com.gestion_employe.backend.Enum.StatutPaiementAchat;
import com.gestion_employe.backend.Enum.TypeAction;
import com.gestion_employe.backend.Enum.TypeEntiteHistorique;
import com.gestion_employe.backend.Mapper.PaiementFournisseurMapper;
import com.gestion_employe.backend.dto.Request.AnnulerPaiementRequestDto;
import com.gestion_employe.backend.dto.Request.PaiementFournisseurRequestDto;
import com.gestion_employe.backend.dto.Request.ValiderPaiementRequestDto;
import com.gestion_employe.backend.dto.Response.PaiementFournisseurResponseDto;
import com.gestion_employe.backend.dto.Response.SoldeAchatResponseDto;
import com.gestion_employe.backend.entity.AchatEntity;
import com.gestion_employe.backend.entity.EmployeEntity;
import com.gestion_employe.backend.entity.PaiementFournisseurEntity;
import com.gestion_employe.backend.repository.PaiementFournisseurRepository;
import com.gestion_employe.backend.service.HistoriqueActionService;
import com.gestion_employe.backend.service.PaiementFournisseurService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.LockModeType;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PaiementFournisseurServiceImpl
        implements PaiementFournisseurService {

    private final PaiementFournisseurRepository paiementRepository;
    private final HistoriqueActionService historiqueActionService;
    private final PaiementFournisseurMapper mapper;

    @PersistenceContext
    private EntityManager entityManager;


    @Override
    @Transactional
    public PaiementFournisseurResponseDto creer(
            PaiementFournisseurRequestDto dto) {

        AchatEntity achat = trouverAchat(dto.getIdAchat());

        BigDecimal total = totalAchat(achat);

        if (total.signum() <= 0) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Cet achat n'a aucun montant à payer."
            );
        }

        BigDecimal reste =
                total.subtract(totalPaye(achat));

        if (reste.signum() <= 0) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Cet achat est déjà soldé."
            );
        }

        if (dto.getMontant().compareTo(reste) > 0) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Le montant (" + dto.getMontant()
                            + ") dépasse le reste à payer (" + reste + ")."
            );
        }

        PaiementFournisseurEntity paiement =
                mapper.toEntity(dto, achat);

        return mapper.toResponseDto(
                paiementRepository.save(paiement)
        );
    }


    @Override
    @Transactional(readOnly = true)
    public PaiementFournisseurResponseDto trouverParId(Long id) {

        return mapper.toResponseDto(
                trouverPaiement(id)
        );
    }


    @Override
    @Transactional(readOnly = true)
    public List<PaiementFournisseurResponseDto> lister() {

        return paiementRepository.findAll()
                .stream()
                .map(mapper::toResponseDto)
                .toList();
    }


    @Override
    @Transactional(readOnly = true)
    public List<PaiementFournisseurResponseDto> listerParAchat(
            Long idAchat) {

        AchatEntity achat = trouverAchat(idAchat);

        return paiementRepository
                .findByAchatEntityOrderByDatePaiementDesc(achat)
                .stream()
                .map(mapper::toResponseDto)
                .toList();
    }


    @Override
    @Transactional
    public PaiementFournisseurResponseDto valider(
            Long id,
            ValiderPaiementRequestDto dto) {

        PaiementFournisseurEntity paiement =
                trouverPaiement(id);

        // Verrouillage de l'achat
        AchatEntity achat = verrouillerAchat(
                paiement.getAchatEntity().getIdAchat()
        );

        entityManager.refresh(paiement);

        if (paiement.getStatut() != StatutPaiement.BROUILLON) {

            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Seul un paiement en brouillon peut être validé."
            );
        }

        EmployeEntity responsable =
                trouverResponsable(dto.getIdResponsable());

        // Recalcul du reste après verrouillage
        BigDecimal reste =
                totalAchat(achat)
                        .subtract(totalPaye(achat));

        if (paiement.getMontant().compareTo(reste) > 0) {

            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Le montant (" + paiement.getMontant()
                            + ") dépasse le reste à payer (" + reste + ")."
            );
        }

        paiement.setStatut(StatutPaiement.VALIDE);
        paiement.setDateValidation(LocalDateTime.now());
        paiement.setResponsableEntity(responsable);

        paiementRepository.saveAndFlush(paiement);

        recalculerAchat(achat);

        // Historique
        historiqueActionService.enregistrer(
                TypeAction.PAIEMENT_FOURNISSEUR_VALIDE,
                TypeEntiteHistorique.PAIEMENT_FOURNISSEUR,
                paiement.getIdPaiementFournisseur(),
                responsable,
                "Paiement fournisseur de "
                        + paiement.getMontant()
                        + " (" + paiement.getModePaiement()
                        + ") validé sur l'achat n°"
                        + achat.getIdAchat()
        );

        return mapper.toResponseDto(paiement);
    }


    @Override
    @Transactional
    public PaiementFournisseurResponseDto annuler(
            Long id,
            AnnulerPaiementRequestDto dto) {

        PaiementFournisseurEntity paiement =
                trouverPaiement(id);

        AchatEntity achat = verrouillerAchat(
                paiement.getAchatEntity().getIdAchat()
        );

        entityManager.refresh(paiement);

        if (paiement.getStatut() == StatutPaiement.ANNULE) {

            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Ce paiement est déjà annulé."
            );
        }

        if (paiement.getStatut() == StatutPaiement.VALIDE
                && (dto.getMotif() == null
                || dto.getMotif().isBlank())) {

            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Le motif est obligatoire pour annuler un paiement validé."
            );
        }

        EmployeEntity responsable =
                trouverResponsable(dto.getIdResponsable());

        boolean etaitValide =
                paiement.getStatut() == StatutPaiement.VALIDE;

        paiement.setStatut(StatutPaiement.ANNULE);
        paiement.setDateAnnulation(LocalDateTime.now());
        paiement.setMotifAnnulation(dto.getMotif());
        paiement.setResponsableAnnulationEntity(responsable);

        paiementRepository.saveAndFlush(paiement);

        recalculerAchat(achat);

        // Historique uniquement si le paiement était validé
        if (etaitValide) {

            historiqueActionService.enregistrer(
                    TypeAction.PAIEMENT_FOURNISSEUR_ANNULE,
                    TypeEntiteHistorique.PAIEMENT_FOURNISSEUR,
                    paiement.getIdPaiementFournisseur(),
                    responsable,
                    "Paiement fournisseur de "
                            + paiement.getMontant()
                            + " annulé sur l'achat n°"
                            + achat.getIdAchat()
                            + " (motif : "
                            + dto.getMotif()
                            + ")"
            );
        }

        return mapper.toResponseDto(paiement);
    }


    @Override
    @Transactional(readOnly = true)
    public SoldeAchatResponseDto soldeAchat(Long idAchat) {

        AchatEntity achat = trouverAchat(idAchat);

        BigDecimal total = totalAchat(achat);
        BigDecimal paye = totalPaye(achat);

        return new SoldeAchatResponseDto(
                achat.getIdAchat(),
                total,
                paye,
                total.subtract(paye),
                calculerStatut(total, paye),
                achat.getDateEcheance()
        );
    }


    private BigDecimal totalAchat(AchatEntity achat) {

        return achat.getTotal() != null
                ? achat.getTotal()
                : BigDecimal.ZERO;
    }


    private BigDecimal totalPaye(AchatEntity achat) {

        BigDecimal paye =
                paiementRepository.sommeParStatut(
                        achat,
                        StatutPaiement.VALIDE
                );

        return paye != null
                ? paye
                : BigDecimal.ZERO;
    }


    private StatutPaiementAchat calculerStatut(
            BigDecimal total,
            BigDecimal paye) {

        if (paye.signum() <= 0) {
            return StatutPaiementAchat.NON_PAYE;
        }

        if (paye.compareTo(total) >= 0) {
            return StatutPaiementAchat.PAYE;
        }

        return StatutPaiementAchat.PARTIELLEMENT_PAYE;
    }


    /*
     * montantPaye, resteAPayer et statutPaiement
     * sont toujours calculés automatiquement.
     */
    private void recalculerAchat(AchatEntity achat) {

        BigDecimal total = totalAchat(achat);
        BigDecimal paye = totalPaye(achat);

        achat.setMontantPaye(paye);

        achat.setResteAPayer(
                total.subtract(paye)
        );

        achat.setStatutPaiement(
                calculerStatut(total, paye)
        );
    }


    private AchatEntity verrouillerAchat(Long idAchat) {

        AchatEntity achat = entityManager.find(
                AchatEntity.class,
                idAchat,
                LockModeType.PESSIMISTIC_WRITE
        );

        // MODIFICATION :
        // on ne bloque plus la validation si deleted = true
        if (achat == null) {

            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "Achat introuvable : " + idAchat
            );
        }

        return achat;
    }


    private AchatEntity trouverAchat(Long idAchat) {

        AchatEntity achat =
                entityManager.find(
                        AchatEntity.class,
                        idAchat
                );

        // MODIFICATION :
        // on vérifie seulement que l'achat existe
        if (achat == null) {

            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "Achat introuvable : " + idAchat
            );
        }

        return achat;
    }


    private EmployeEntity trouverResponsable(
            Long idResponsable) {

        EmployeEntity responsable =
                entityManager.find(
                        EmployeEntity.class,
                        idResponsable
                );

        if (responsable == null
                || responsable.isDeleted()) {

            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "Responsable introuvable : "
                            + idResponsable
            );
        }

        return responsable;
    }


    private PaiementFournisseurEntity trouverPaiement(
            Long id) {

        return paiementRepository
                .findById(id)
                .orElseThrow(() ->
                        new ResponseStatusException(
                                HttpStatus.NOT_FOUND,
                                "Paiement introuvable : " + id
                        )
                );
    }
}