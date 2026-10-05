package com.gestion_employe.backend.service;

import com.gestion_employe.backend.dto.Request.LigneAchatRequestDto;
import com.gestion_employe.backend.dto.Response.LigneAchatResponseDto;

import java.util.List;

public interface LigneAchatService {

    LigneAchatResponseDto add(LigneAchatRequestDto dto);

    LigneAchatResponseDto update(
            Long id,
            LigneAchatRequestDto dto
    );

    LigneAchatResponseDto getById(Long id);

    List<LigneAchatResponseDto> getAll();

    List<LigneAchatResponseDto> getByAchat(Long idAchat);

    void delete(Long id);
}

