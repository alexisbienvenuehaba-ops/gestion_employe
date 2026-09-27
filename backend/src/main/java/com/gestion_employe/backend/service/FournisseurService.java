package com.gestion_employe.backend.service;

import com.gestion_employe.backend.dto.Request.EmployeRequestDto;
import com.gestion_employe.backend.dto.Request.FournisseurRequestDto;
import com.gestion_employe.backend.dto.Response.EmployeResponseDto;
import com.gestion_employe.backend.dto.Response.FournisseurResponseDto;

import java.util.List;

public interface FournisseurService {

    List<FournisseurResponseDto> getAll();
    FournisseurResponseDto getById(Long id);

    FournisseurResponseDto create(FournisseurRequestDto request);

    FournisseurResponseDto update(Long id, FournisseurRequestDto request);
    void delete(Long id);
}
