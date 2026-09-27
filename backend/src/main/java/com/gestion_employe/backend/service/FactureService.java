package com.gestion_employe.backend.service;

import com.gestion_employe.backend.dto.Request.FactureRequestDto;
import com.gestion_employe.backend.dto.Response.FactureResponseDto;

import java.util.List;

public interface FactureService {
    List<FactureResponseDto> getAll();
    FactureRequestDto getById(Long id);
    FactureResponseDto create(FactureRequestDto request);
    FactureResponseDto update(Long id, FactureRequestDto request);
    void delete(Long id);
}
