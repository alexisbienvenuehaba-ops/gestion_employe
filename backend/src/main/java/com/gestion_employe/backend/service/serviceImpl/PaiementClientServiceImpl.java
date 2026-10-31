package com.gestion_employe.backend.service.serviceImpl;

import com.gestion_employe.backend.Enum.StatutPaiement;
import com.gestion_employe.backend.Enum.StatutPaiementFacture;
import com.gestion_employe.backend.Enum.TypeAction;
import com.gestion_employe.backend.Enum.TypeEntiteHistorique;
import com.gestion_employe.backend.Mapper.PaiementClientMapper;
import com.gestion_employe.backend.dto.Request.AnnulerPaiementRequestDto;
import com.gestion_employe.backend.dto.Request.PaiementClientRequestDto;
import com.gestion_employe.backend.dto.Request.ValiderPaiementRequestDto;
import com.gestion_employe.backend.dto.Response.PaiementClientResponseDto;
import com.gestion_employe.backend.dto.Response.SoldeFactureResponseDto;
import com.gestion_employe.backend.entity.EmployeEntity;
import com.gestion_employe.backend.entity.FactureEntity;
import com.gestion_employe.backend.entity.PaiementClientEntity;
import com.gestion_employe.backend.repository.PaiementClientRepository;
import com.gestion_employe.backend.service.HistoriqueActionService;
import com.gestion_employe.backend.service.PaiementClientService;
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
public class PaiementClientServiceImpl implements PaiementClientService {

    private final PaiementClientRepository paiementRepository;
    private final HistoriqueActionService historiqueActionService;
    private final PaiementClientMapper mapper;

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    @Transactional
    public PaiementClientResponseDto creer(PaiementClientRequestDto dto) {
        FactureEntity facture = trouverFacture(dto.getIdFacture());

        BigDecimal total = totalFacture(facture);
        if (total.signum() <= 0) {
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "Cette facture n'a aucun montant à payer.");
        }
        BigDecimal reste = total.subtract(totalPaye(facture));
        if (reste.signum() <= 0) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Cette facture est déjà soldée.");
        }
        if (dto.getMontant().compareTo(reste) > 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Le montant (" + dto.getMontant() + ") dépasse le reste à payer (" + reste + ").");
        }

        PaiementClientEntity paiement = mapper.toEntity(dto, facture);
        return mapper.toResponseDto(paiementRepository.save(paiement));
    }

    @Override
    @Transactional(readOnly = true)
    public PaiementClientResponseDto trouverParId(Long id) {
        return mapper.toResponseDto(trouverPaiement(id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<PaiementClientResponseDto> lister() {
        return paiementRepository.findAll().stream().map(mapper::toResponseDto).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<PaiementClientResponseDto> listerParFacture(Long idFacture) {
        FactureEntity facture = trouverFacture(idFacture);
        return paiementRepository.findByFactureEntityOrderByDatePaiementDesc(facture)
                .stream().map(mapper::toResponseDto).toList();
    }

    @Override
    @Transactional
    public PaiementClientResponseDto valider(Long id, ValiderPaiementRequestDto dto) {
        PaiementClientEntity paiement = trouverPaiement(id);

        // Verrou sur la facture : un seul paiement validé à la fois pour une même facture.
        FactureEntity facture = verrouillerFacture(paiement.getFactureEntity().getIdFacture());
        entityManager.refresh(paiement);

        if (paiement.getStatut() != StatutPaiement.BROUILLON) {
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "Seul un paiement en brouillon peut être validé.");
        }

        EmployeEntity responsable = trouverResponsable(dto.getIdResponsable());

        // Le reste à payer est re-contrôlé : d'autres paiements ont pu être validés depuis la saisie.
        BigDecimal reste = totalFacture(facture).subtract(totalPaye(facture));
        if (paiement.getMontant().compareTo(reste) > 0) {
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "Le montant (" + paiement.getMontant() + ") dépasse le reste à payer (" + reste + ").");
        }

        paiement.setStatut(StatutPaiement.VALIDE);
        paiement.setDateValidation(LocalDateTime.now());
        paiement.setResponsableEntity(responsable);
        paiementRepository.saveAndFlush(paiement);

        recalculerStatutFacture(facture);

        historiqueActionService.enregistrer(
                TypeAction.PAIEMENT_CLIENT_VALIDE, TypeEntiteHistorique.PAIEMENT_CLIENT,
                paiement.getIdPaiementClient(), responsable,
                "Paiement de " + paiement.getMontant() + " (" + paiement.getModePaiement()
                        + ") validé sur la facture " + facture.getNumeroFacture());

        return mapper.toResponseDto(paiement);
    }

    @Override
    @Transactional
    public PaiementClientResponseDto annuler(Long id, AnnulerPaiementRequestDto dto) {
        PaiementClientEntity paiement = trouverPaiement(id);

        FactureEntity facture = verrouillerFacture(paiement.getFactureEntity().getIdFacture());
        entityManager.refresh(paiement);

        if (paiement.getStatut() == StatutPaiement.ANNULE) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Ce paiement est déjà annulé.");
        }
        if (paiement.getStatut() == StatutPaiement.VALIDE
                && (dto.getMotif() == null || dto.getMotif().isBlank())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Le motif est obligatoire pour annuler un paiement validé.");
        }

        EmployeEntity responsable = trouverResponsable(dto.getIdResponsable());

        boolean etaitValide = paiement.getStatut() == StatutPaiement.VALIDE;

        paiement.setStatut(StatutPaiement.ANNULE);
        paiement.setDateAnnulation(LocalDateTime.now());
        paiement.setMotifAnnulation(dto.getMotif());
        paiement.setResponsableAnnulationEntity(responsable);
        paiementRepository.saveAndFlush(paiement);

        recalculerStatutFacture(facture);

        if (etaitValide) {
            historiqueActionService.enregistrer(
                    TypeAction.PAIEMENT_CLIENT_ANNULE, TypeEntiteHistorique.PAIEMENT_CLIENT,
                    paiement.getIdPaiementClient(), responsable,
                    "Paiement de " + paiement.getMontant() + " annulé sur la facture "
                            + facture.getNumeroFacture() + " (motif : " + dto.getMotif() + ")");
        }

        return mapper.toResponseDto(paiement);
    }

    @Override
    @Transactional(readOnly = true)
    public SoldeFactureResponseDto soldeFacture(Long idFacture) {
        FactureEntity facture = trouverFacture(idFacture);
        BigDecimal total = totalFacture(facture);
        BigDecimal paye = totalPaye(facture);
        return new SoldeFactureResponseDto(
                facture.getIdFacture(),
                facture.getNumeroFacture(),
                total,
                paye,
                total.subtract(paye),
                calculerStatut(total, paye));
    }

    // ------------------------------------------------------------------ Outils

    private BigDecimal totalFacture(FactureEntity facture) {
        BigDecimal total = paiementRepository.totalFacture(facture);
        return total != null ? total : BigDecimal.ZERO;
    }

    private BigDecimal totalPaye(FactureEntity facture) {
        BigDecimal paye = paiementRepository.sommeParStatut(facture, StatutPaiement.VALIDE);
        return paye != null ? paye : BigDecimal.ZERO;
    }

    private StatutPaiementFacture calculerStatut(BigDecimal total, BigDecimal paye) {
        if (paye.signum() <= 0) {
            return StatutPaiementFacture.NON_PAYEE;
        }
        if (paye.compareTo(total) >= 0) {
            return StatutPaiementFacture.PAYEE;
        }
        return StatutPaiementFacture.PARTIELLEMENT_PAYEE;
    }

    /** Le statut de paiement de la facture est toujours calculé, jamais saisi. */
    private void recalculerStatutFacture(FactureEntity facture) {
        facture.setStatutPaiement(calculerStatut(totalFacture(facture), totalPaye(facture)));
    }

    private FactureEntity verrouillerFacture(Long idFacture) {
        FactureEntity facture = entityManager.find(FactureEntity.class, idFacture, LockModeType.PESSIMISTIC_WRITE);
        if (facture == null || facture.isDeleted()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Facture introuvable : " + idFacture);
        }
        return facture;
    }

    private FactureEntity trouverFacture(Long idFacture) {
        FactureEntity facture = entityManager.find(FactureEntity.class, idFacture);
        if (facture == null || facture.isDeleted()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Facture introuvable : " + idFacture);
        }
        return facture;
    }

    private EmployeEntity trouverResponsable(Long idResponsable) {
        EmployeEntity responsable = entityManager.find(EmployeEntity.class, idResponsable);
        if (responsable == null || responsable.isDeleted()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    "Responsable introuvable : " + idResponsable);
        }
        return responsable;
    }

    private PaiementClientEntity trouverPaiement(Long id) {
        return paiementRepository.findById(id).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Paiement introuvable : " + id));
    }
}