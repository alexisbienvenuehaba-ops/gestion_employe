package com.gestion_employe.backend.repository;

import com.gestion_employe.backend.entity.LigneFactureEntity;
import com.gestion_employe.backend.entity.ProduitEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface LigneFactureRepository extends JpaRepository<LigneFactureEntity,Long> {

    List<LigneFactureEntity> findAllByDeletedFalse();
    Optional<LigneFactureEntity> findByIdLigneFactureAndDeletedFalse(Long idProduit);
}
