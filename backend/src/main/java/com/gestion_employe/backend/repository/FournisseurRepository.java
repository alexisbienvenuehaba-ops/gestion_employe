package com.gestion_employe.backend.repository;

import com.gestion_employe.backend.entity.EmployeEntity;
import com.gestion_employe.backend.entity.FournisseurEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FournisseurRepository extends JpaRepository<FournisseurEntity,Long> {

    List<FournisseurEntity> findAllByDeletedFalse();

    java.util.Optional<FournisseurEntity> findByIdFournisseurAndDeletedFalse(Long id);
}
