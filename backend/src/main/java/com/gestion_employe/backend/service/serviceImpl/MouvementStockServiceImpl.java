package com.gestion_employe.backend.service.serviceImpl;

import com.gestion_employe.backend.Enum.OrigineMouvement;
import com.gestion_employe.backend.Enum.TypeAction;
import com.gestion_employe.backend.Enum.TypeEntiteHistorique;
import com.gestion_employe.backend.Enum.TypeMouvement;
import com.gestion_employe.backend.Mapper.MouvementStockMapper;
import com.gestion_employe.backend.dto.Request.MouvementStockRequestDto;
import com.gestion_employe.backend.dto.Response.MouvementStockResponseDto;
import com.gestion_employe.backend.dto.Response.StockProduitResponseDto;
import com.gestion_employe.backend.entity.EmployeEntity;
import com.gestion_employe.backend.entity.LigneReceptionAchatEntity;
import com.gestion_employe.backend.entity.MouvementStockEntity;
import com.gestion_employe.backend.entity.ProduitEntity;
import com.gestion_employe.backend.repository.MouvementStockRepository;
import com.gestion_employe.backend.service.HistoriqueActionService;
import com.gestion_employe.backend.service.MouvementStockService;
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
public class MouvementStockServiceImpl implements MouvementStockService {

    private final MouvementStockRepository mouvementRepository;
    private final HistoriqueActionService historiqueActionService;
    private final MouvementStockMapper mapper;

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    @Transactional
    public MouvementStockResponseDto enregistrer(Long idProduit, TypeMouvement type, int quantite,
                                                 OrigineMouvement origine,
                                                 LigneReceptionAchatEntity ligneReception,
                                                 EmployeEntity responsable, String motif) {
        if (quantite <= 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "La quantité d'un mouvement doit être supérieure à 0.");
        }

        // Verrou sur le produit : deux mouvements simultanés ne peuvent pas se marcher dessus.
        ProduitEntity produit = entityManager.find(ProduitEntity.class, idProduit, LockModeType.PESSIMISTIC_WRITE);
        if (produit == null || produit.isDeleted()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Produit introuvable : " + idProduit);
        }

        int stockCourant = produit.getQuantiteStock() != null ? produit.getQuantiteStock() : 0;
        int nouveauStock;
        if (type == TypeMouvement.ENTREE) {
            nouveauStock = stockCourant + quantite;
        } else {
            if (quantite > stockCourant) {
                throw new ResponseStatusException(HttpStatus.CONFLICT,
                        "Stock insuffisant : " + stockCourant + " disponible, " + quantite + " demandé.");
            }
            nouveauStock = stockCourant - quantite;
        }
        produit.setQuantiteStock(nouveauStock);

        MouvementStockEntity mouvement = new MouvementStockEntity();
        mouvement.setProduitEntity(produit);
        mouvement.setTypeMouvement(type);
        mouvement.setQuantite(quantite);
        mouvement.setDateMouvement(LocalDateTime.now());
        mouvement.setOrigine(origine);
        mouvement.setLigneReceptionAchatEntity(ligneReception);
        mouvement.setResponsableEntity(responsable);
        mouvement.setMotif(motif);

        return mapper.toResponseDto(mouvementRepository.save(mouvement));
    }

    @Override
    @Transactional
    public MouvementStockResponseDto creerCorrection(MouvementStockRequestDto dto) {
        EmployeEntity responsable = entityManager.find(EmployeEntity.class, dto.getIdResponsable());
        if (responsable == null || responsable.isDeleted()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    "Responsable introuvable : " + dto.getIdResponsable());
        }
        MouvementStockResponseDto resultat = enregistrer(dto.getIdProduit(), dto.getTypeMouvement(),
                dto.getQuantite(), OrigineMouvement.CORRECTION, null, responsable, dto.getMotif());

        historiqueActionService.enregistrer(
                TypeAction.CORRECTION_STOCK, TypeEntiteHistorique.MOUVEMENT_STOCK,
                resultat.getIdMouvementStock(), responsable,
                "Correction de stock : " + dto.getTypeMouvement() + " de " + dto.getQuantite()
                        + " unité(s) du produit n°" + dto.getIdProduit() + " (motif : " + dto.getMotif() + ")");
        return resultat;
    }

    @Override
    @Transactional(readOnly = true)
    public List<MouvementStockResponseDto> lister() {
        return mouvementRepository.findAllByOrderByDateMouvementDesc()
                .stream().map(mapper::toResponseDto).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<MouvementStockResponseDto> listerParProduit(Long idProduit) {
        trouverProduit(idProduit);
        return mouvementRepository.findByProduitEntityIdProduitOrderByDateMouvementDesc(idProduit)
                .stream().map(mapper::toResponseDto).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public StockProduitResponseDto stockProduit(Long idProduit) {
        ProduitEntity produit = trouverProduit(idProduit);
        Long calcule = mouvementRepository.calculerStock(produit, TypeMouvement.ENTREE);
        return new StockProduitResponseDto(produit.getIdProduit(), produit.getNom(),
                produit.getQuantiteStock(), calcule);
    }

    private ProduitEntity trouverProduit(Long idProduit) {
        ProduitEntity produit = entityManager.find(ProduitEntity.class, idProduit);
        if (produit == null || produit.isDeleted()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Produit introuvable : " + idProduit);
        }
        return produit;
    }
}