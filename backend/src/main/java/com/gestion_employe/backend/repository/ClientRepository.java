package com.gestion_employe.backend.repository;

import com.gestion_employe.backend.entity.ClientEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ClientRepository extends JpaRepository<ClientEntity, Long> {

    List<ClientEntity> findAllByDeletedFalse();

    java.util.Optional<ClientEntity> findByIdAndDeletedFalse(Long id);
}