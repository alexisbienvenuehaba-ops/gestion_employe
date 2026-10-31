package com.gestion_employe.backend.repository;

import com.gestion_employe.backend.entity.AchatEntity;
import com.gestion_employe.backend.entity.PaiementFournisseurEntity;
import com.gestion_employe.backend.Enum.StatutPaiement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.List;

public interface PaiementFournisseurRepository
        extends JpaRepository<PaiementFournisseurEntity, Long> {

    List<PaiementFournisseurEntity>
    findByAchatEntityOrderByDatePaiementDesc(AchatEntity achat);

    List<PaiementFournisseurEntity>
    findAllByOrderByIdPaiementFournisseurDesc();

    /**
     * Somme des paiements d'un achat ayant un statut donné.
     */
    @Query("""
           select sum(p.montant)
           from PaiementFournisseurEntity p
           where p.achatEntity = :achat
             and p.statut = :statut
           """)
    BigDecimal sommeParStatut(
            @Param("achat") AchatEntity achat,
            @Param("statut") StatutPaiement statut
    );
}