package com.gestion_employe.backend.service;

import com.gestion_employe.backend.dto.Request.LigneReceptionAchatRequestDto;
import com.gestion_employe.backend.dto.Response.LigneReceptionAchatResponseDto;

import java.util.List;

public interface LigneReceptionAchatService {

    LigneReceptionAchatResponseDto ajouter(Long idReception, LigneReceptionAchatRequestDto dto);

    LigneReceptionAchatResponseDto modifier(Long idLigneReception, LigneReceptionAchatRequestDto dto);

    void supprimer(Long idLigneReception);

    List<LigneReceptionAchatResponseDto> listerParReception(Long idReception);
}