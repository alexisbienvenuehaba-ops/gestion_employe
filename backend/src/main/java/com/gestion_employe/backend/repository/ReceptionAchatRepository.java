package com.gestion_employe.backend.repository;

import com.gestion_employe.backend.entity.AchatEntity;
import com.gestion_employe.backend.entity.ReceptionAchatEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReceptionAchatRepository extends JpaRepository<ReceptionAchatEntity, Long> {

    List<ReceptionAchatEntity> findByAchatEntityOrderByDateReceptionDesc(AchatEntity achatEntity);
}