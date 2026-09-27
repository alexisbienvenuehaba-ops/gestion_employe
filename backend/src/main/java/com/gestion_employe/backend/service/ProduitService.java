package com.gestion_employe.backend.service;

import com.gestion_employe.backend.dto.Request.ProduitRequestDto;
import com.gestion_employe.backend.dto.Response.ProduitResponseDto;

import java.util.List;

public interface ProduitService {
    List<ProduitResponseDto> getAll();
    ProduitResponseDto getById(Long id);
    ProduitResponseDto create(ProduitRequestDto request);
    ProduitResponseDto update(Long id, ProduitRequestDto request);
    void delete(Long id);
}