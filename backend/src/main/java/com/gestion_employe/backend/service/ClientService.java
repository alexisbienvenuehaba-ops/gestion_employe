package com.gestion_employe.backend.service;

import com.gestion_employe.backend.dto.Response.ClientResponseDto;
import com.gestion_employe.backend.dto.Request.ClientRequestDto;

import java.util.List;
public interface ClientService {
    List<ClientResponseDto> getAll();
    ClientResponseDto getById(Long id);
    ClientResponseDto create(ClientRequestDto request);
    ClientResponseDto update(Long id, ClientRequestDto request);
    void delete(Long id);
}
