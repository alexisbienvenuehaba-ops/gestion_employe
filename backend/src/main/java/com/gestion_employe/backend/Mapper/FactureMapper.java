package com.gestion_employe.backend.Mapper;

import com.gestion_employe.backend.dto.Request.FactureRequestDto;
import com.gestion_employe.backend.dto.Response.FactureResponseDto;
import com.gestion_employe.backend.entity.ClientEntity;
import com.gestion_employe.backend.entity.EmployeEntity;
import com.gestion_employe.backend.entity.FactureEntity;
import com.gestion_employe.backend.repository.ClientRepository;
import com.gestion_employe.backend.repository.EmployeRepository;
import org.springframework.stereotype.Component;

@Component
public class FactureMapper {

    private final ClientRepository clientRepository;
    private final EmployeRepository employeRepository;

    public FactureMapper(ClientRepository clientRepository, EmployeRepository employeRepository) {
        this.clientRepository = clientRepository;
        this.employeRepository = employeRepository;
    }

    public FactureEntity toEntity(FactureRequestDto request) {
        FactureEntity factureEntity = new FactureEntity();

        factureEntity.setStatut(request.getStatut());
        factureEntity.setDateFacture(request.getDateFacture());

        ClientEntity clientEntity = clientRepository
                .findByIdAndDeletedFalse(request.getId())
                .orElseThrow(() -> new RuntimeException(
                        "Client introuvable avec l'id " + request.getId()));

        factureEntity.setClientEntity(clientEntity);

        EmployeEntity employeEntity = employeRepository
                .findByIdEmployeAndDeletedFalse(request.getIdEmploye())
                .orElseThrow(() -> new RuntimeException(
                        "Employé introuvable avec l'id " + request.getIdEmploye()));

        factureEntity.setEmployeEntity(employeEntity);

        return factureEntity;
    }

    public FactureResponseDto toResponse(FactureEntity entity) {
        return new FactureResponseDto(
                entity.getIdFacture(),
                entity.getStatut(),
                entity.getDateFacture(),
                entity.getClientEntity() != null ? entity.getClientEntity().getId() : null,
                entity.getClientEntity() != null ? entity.getClientEntity().getNom() : null,
                entity.getEmployeEntity() != null ? entity.getEmployeEntity().getIdEmploye() : null,
                entity.getEmployeEntity() != null ? entity.getEmployeEntity().getNom() : null
        );
    }
}

