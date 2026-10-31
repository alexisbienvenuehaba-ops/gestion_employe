package com.gestion_employe.backend.service;

import com.gestion_employe.backend.dto.Request.AnnulerPaiementRequestDto;
import com.gestion_employe.backend.dto.Request.PaiementClientRequestDto;
import com.gestion_employe.backend.dto.Request.ValiderPaiementRequestDto;
import com.gestion_employe.backend.dto.Response.PaiementClientResponseDto;
import com.gestion_employe.backend.dto.Response.SoldeFactureResponseDto;

import java.util.List;

public interface PaiementClientService {

    PaiementClientResponseDto creer(PaiementClientRequestDto dto);

    PaiementClientResponseDto trouverParId(Long id);

    List<PaiementClientResponseDto> lister();

    List<PaiementClientResponseDto> listerParFacture(Long idFacture);

    PaiementClientResponseDto valider(Long id, ValiderPaiementRequestDto dto);

    PaiementClientResponseDto annuler(Long id, AnnulerPaiementRequestDto dto);

    SoldeFactureResponseDto soldeFacture(Long idFacture);
}