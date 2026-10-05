package com.gestion_employe.backend.repository;

import com.gestion_employe.backend.entity.AchatEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AchatRepository extends JpaRepository<AchatEntity, Long> {

    List<AchatEntity> findAllByDeletedFalse();
}