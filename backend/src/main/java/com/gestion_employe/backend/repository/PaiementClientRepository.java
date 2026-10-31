package com.gestion_employe.backend.repository;

import com.gestion_employe.backend.entity.FactureEntity;
import com.gestion_employe.backend.entity.PaiementClientEntity;
import com.gestion_employe.backend.Enum.StatutPaiement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.List;

public interface PaiementClientRepository extends JpaRepository<PaiementClientEntity, Long> {

    List<PaiementClientEntity> findByFactureEntityOrderByDatePaiementDesc(FactureEntity facture);

    /** Total d'une facture = somme quantité × prix unitaire de ses lignes non supprimées (null s'il n'y en a pas). */
    @Query("""
            select sum(l.quantite * l.prixUnitaire)
            from LigneFactureEntity l
            where l.factureEntity = :facture
              and l.deleted = false
            """)
    BigDecimal totalFacture(@Param("facture") FactureEntity facture);

    /** Somme des paiements d'une facture ayant un statut donné (null s'il n'y en a pas). */
    @Query("""
            select sum(p.montant)
            from PaiementClientEntity p
            where p.factureEntity = :facture
              and p.statut = :statut
            """)
    BigDecimal sommeParStatut(@Param("facture") FactureEntity facture,
                              @Param("statut") StatutPaiement statut);
}