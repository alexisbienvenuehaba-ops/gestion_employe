package com.gestion_employe.backend.service;

import com.gestion_employe.backend.dto.Request.LigneFactureRequestDto;
import com.gestion_employe.backend.dto.Response.LigneFactureResponseDto;

import java.util.List;

public interface LigneFactureService {
    List<LigneFactureResponseDto>getAll();
    LigneFactureResponseDto getById(Long id);

    LigneFactureResponseDto create(LigneFactureRequestDto request);

    LigneFactureResponseDto update(Long id ,LigneFactureRequestDto request);
    void delete(Long id);
}
