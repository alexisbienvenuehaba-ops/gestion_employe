package com.gestion_employe.backend.service;

import com.gestion_employe.backend.dto.Request.AchatRequestDto;
import com.gestion_employe.backend.dto.Response.AchatResponseDto;

import java.util.List;

public interface AchatService {

    AchatResponseDto add(AchatRequestDto dto);

    AchatResponseDto update(Long id, AchatRequestDto dto);

    AchatResponseDto getById(Long id);

    List<AchatResponseDto> getAll();

    void delete(Long id);
}