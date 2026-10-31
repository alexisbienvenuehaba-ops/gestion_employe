package com.gestion_employe.backend.repository;

import com.gestion_employe.backend.Enum.DecisionRefus;
import com.gestion_employe.backend.Enum.StatutReception;
import com.gestion_employe.backend.entity.AchatEntity;
import com.gestion_employe.backend.entity.LigneAchatEntity;
import com.gestion_employe.backend.entity.LigneReceptionAchatEntity;
import com.gestion_employe.backend.entity.ReceptionAchatEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface LigneReceptionAchatRepository extends JpaRepository<LigneReceptionAchatEntity, Long> {

    List<LigneReceptionAchatEntity> findByReceptionAchatEntity(ReceptionAchatEntity reception);

    boolean existsByReceptionAchatEntityAndLigneAchatEntity(ReceptionAchatEntity reception,
                                                            LigneAchatEntity ligneAchat);

    /** Quantités acceptées au contrôle, dans les réceptions validées, pour une ligne d'achat. */
    @Query("""
            select coalesce(sum(l.quantiteAcceptee), 0L)
            from LigneReceptionAchatEntity l
            where l.ligneAchatEntity = :ligneAchat
              and l.receptionAchatEntity.statut = :statut
            """)
    Long sommeAcceptees(@Param("ligneAchat") LigneAchatEntity ligneAchat,
                        @Param("statut") StatutReception statut);

    /** Quantités refusées puis finalement acceptées exceptionnellement (elles comptent comme reçues). */
    @Query("""
            select coalesce(sum(l.quantiteRefusee), 0L)
            from LigneReceptionAchatEntity l
            where l.ligneAchatEntity = :ligneAchat
              and l.receptionAchatEntity.statut = :statut
              and l.decisionRefus = :decision
            """)
    Long sommeAccepteesExceptionnellement(@Param("ligneAchat") LigneAchatEntity ligneAchat,
                                          @Param("statut") StatutReception statut,
                                          @Param("decision") DecisionRefus decision);

    /** Reste-t-il des refus en attente de décision pour cet achat ? */
    @Query("""
            select count(l) > 0
            from LigneReceptionAchatEntity l
            where l.receptionAchatEntity.achatEntity = :achat
              and l.receptionAchatEntity.statut = :statut
              and l.decisionRefus = :decision
            """)
    boolean existeRefusEnAttente(@Param("achat") AchatEntity achat,
                                 @Param("statut") StatutReception statut,
                                 @Param("decision") DecisionRefus decision);
}