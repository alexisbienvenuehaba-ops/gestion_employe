package com.gestion_employe.backend.repository;

import com.gestion_employe.backend.entity.FactureEntity;
import com.gestion_employe.backend.entity.ProduitEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FactureRepository extends JpaRepository<FactureEntity,Long> {

    List<FactureEntity> findAllByDeletedFalse();
    Optional<FactureEntity> findByIdFactureAndDeletedFalse(Long idProduit);
}
