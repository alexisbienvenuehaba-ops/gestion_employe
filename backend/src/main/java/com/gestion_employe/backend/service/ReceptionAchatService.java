package com.gestion_employe.backend.service;

import com.gestion_employe.backend.dto.Request.DecisionRefusRequestDto;
import com.gestion_employe.backend.dto.Request.ReceptionAchatRequestDto;
import com.gestion_employe.backend.dto.Request.ValiderReceptionRequestDto;
import com.gestion_employe.backend.dto.Response.LigneReceptionAchatResponseDto;
import com.gestion_employe.backend.dto.Response.ReceptionAchatResponseDto;

import java.util.List;

public interface ReceptionAchatService {

    ReceptionAchatResponseDto creer(ReceptionAchatRequestDto dto);

    ReceptionAchatResponseDto trouverParId(Long id);

    List<ReceptionAchatResponseDto> lister();

    List<ReceptionAchatResponseDto> listerParAchat(Long idAchat);

    ReceptionAchatResponseDto annuler(Long id);

    /** Valide un brouillon : entrée en stock des quantités acceptées. */
    ReceptionAchatResponseDto valider(Long id, ValiderReceptionRequestDto dto);

    /** Décision sur des produits refusés (retour fournisseur ou acceptation exceptionnelle). */
    LigneReceptionAchatResponseDto deciderRefus(Long idLigneReception, DecisionRefusRequestDto dto);
}