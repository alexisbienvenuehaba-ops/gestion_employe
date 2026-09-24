package com.gestion_employe.backend.Mapper;

import com.gestion_employe.backend.dto.Request.ClientRequestDto;
import com.gestion_employe.backend.dto.Request.EmployeRequestDto;
import com.gestion_employe.backend.dto.Response.ClientResponseDto;
import com.gestion_employe.backend.dto.Response.EmployeResponseDto;
import com.gestion_employe.backend.entity.ClientEntity;
import com.gestion_employe.backend.entity.EmployeEntity;
import org.springframework.stereotype.Component;

@Component
public class EmployeMapper {
    public EmployeEntity toEntity(EmployeRequestDto request) {
        EmployeEntity employe = new EmployeEntity();
        employe.setNom(request.getNom());
        employe.setPrenom(request.getPrenom());
        employe.setEmail(request.getEmail());
        employe.setTelephone(request.getTelephone());
        employe.setPoste(request.getPoste());
        return employe;

        }
    public EmployeResponseDto toResponse(EmployeEntity entity) {
        return new EmployeResponseDto(
                entity.getIdEmploye(),
                entity.getNom(),
                entity.getPrenom(),
                entity.getEmail(),
                entity.getTelephone(),
                entity.getPoste()
        );
    }

}
