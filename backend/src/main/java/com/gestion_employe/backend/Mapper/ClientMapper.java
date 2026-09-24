package com.gestion_employe.backend.Mapper;

import com.gestion_employe.backend.dto.Request.ClientRequestDto;
import com.gestion_employe.backend.dto.Response.ClientResponseDto;
import com.gestion_employe.backend.entity.ClientEntity;
import org.springframework.stereotype.Component;

@Component

public class ClientMapper {

    public ClientEntity toEntity(ClientRequestDto request) {
        ClientEntity client = new ClientEntity();
        client.setNom(request.getNom());
        client.setPrenom(request.getPrenom());
        client.setEmail(request.getEmail());
        client.setTelephone(request.getTelephone());
        client.setEntreprise(request.getEntreprise());
        return client;
    }

    public ClientResponseDto toResponse(ClientEntity entity) {
        return new ClientResponseDto(
                entity.getId(),
                entity.getNom(),
                entity.getPrenom(),
                entity.getEmail(),
                entity.getTelephone(),
                entity.getEntreprise()
        );
    }
}

