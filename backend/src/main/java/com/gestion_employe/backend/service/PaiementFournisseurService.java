package com.gestion_employe.backend.service;

import com.gestion_employe.backend.dto.Request.AnnulerPaiementRequestDto;
import com.gestion_employe.backend.dto.Request.PaiementFournisseurRequestDto;
import com.gestion_employe.backend.dto.Request.ValiderPaiementRequestDto;
import com.gestion_employe.backend.dto.Response.PaiementFournisseurResponseDto;
import com.gestion_employe.backend.dto.Response.SoldeAchatResponseDto;

import java.util.List;

public interface PaiementFournisseurService {

    PaiementFournisseurResponseDto creer(PaiementFournisseurRequestDto dto);

    PaiementFournisseurResponseDto trouverParId(Long id);

    List<PaiementFournisseurResponseDto> lister();

    List<PaiementFournisseurResponseDto> listerParAchat(Long idAchat);

    PaiementFournisseurResponseDto valider(Long id, ValiderPaiementRequestDto dto);

    PaiementFournisseurResponseDto annuler(Long id, AnnulerPaiementRequestDto dto);

    SoldeAchatResponseDto soldeAchat(Long idAchat);
}