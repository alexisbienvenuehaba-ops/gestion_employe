package com.gestion_employe.backend.repository;

import com.gestion_employe.backend.entity.MouvementStockEntity;
import com.gestion_employe.backend.entity.ProduitEntity;
import com.gestion_employe.backend.Enum.TypeMouvement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MouvementStockRepository extends JpaRepository<MouvementStockEntity, Long> {

    List<MouvementStockEntity> findAllByOrderByDateMouvementDesc();

    List<MouvementStockEntity> findByProduitEntityIdProduitOrderByDateMouvementDesc(Long idProduit);

    /** Stock d'un produit calculé depuis le journal = somme des entrées - somme des sorties. */
    @Query("""
            select coalesce(sum(case when m.typeMouvement = :entree then m.quantite else -m.quantite end), 0L)
            from MouvementStockEntity m
            where m.produitEntity = :produit
            """)
    Long calculerStock(@Param("produit") ProduitEntity produit,
                       @Param("entree") TypeMouvement entree);
}