package com.gestion_employe.backend.repository;

import com.gestion_employe.backend.entity.ProduitEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ProduitRepository extends JpaRepository<ProduitEntity, Long> {
    List<ProduitEntity> findAllByDeletedFalse();
    Optional<ProduitEntity> findByIdProduitAndDeletedFalse(Long idProduit);
}