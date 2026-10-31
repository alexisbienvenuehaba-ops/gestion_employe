package com.gestion_employe.backend.repository;

import com.gestion_employe.backend.entity.HistoriqueActionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface HistoriqueActionRepository
        extends JpaRepository<HistoriqueActionEntity, Long>, JpaSpecificationExecutor<HistoriqueActionEntity> {
}