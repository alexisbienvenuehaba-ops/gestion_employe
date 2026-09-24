package com.gestion_employe.backend.service;

import com.gestion_employe.backend.dto.Request.ClientRequestDto;
import com.gestion_employe.backend.dto.Request.EmployeRequestDto;
import com.gestion_employe.backend.dto.Response.ClientResponseDto;
import com.gestion_employe.backend.dto.Response.EmployeResponseDto;

import java.util.List;

public interface EmployeService {

    List<EmployeResponseDto> getAll();
    EmployeResponseDto getById(Long id);

    EmployeResponseDto create(EmployeRequestDto request);

    EmployeResponseDto update(Long id, EmployeRequestDto request);
    void delete(Long id);
}
