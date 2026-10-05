package com.gestion_employe.backend.repository;

import com.gestion_employe.backend.entity.LigneAchatEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LigneAchatRepository
        extends JpaRepository<LigneAchatEntity, Long> {

    List<LigneAchatEntity> findAllByAchatEntityIdAchat(Long idAchat);
}