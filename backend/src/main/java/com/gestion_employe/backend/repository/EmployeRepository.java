package com.gestion_employe.backend.repository;

import com.gestion_employe.backend.entity.ClientEntity;
import com.gestion_employe.backend.entity.EmployeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EmployeRepository extends JpaRepository<EmployeEntity,Long> {
    List<EmployeEntity> findAllByDeletedFalse();

    java.util.Optional<EmployeEntity> findByIdEmployeAndDeletedFalse(Long id);
}
