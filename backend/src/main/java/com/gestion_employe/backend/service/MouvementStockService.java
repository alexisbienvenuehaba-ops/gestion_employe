package com.gestion_employe.backend.service;

import com.gestion_employe.backend.dto.Request.MouvementStockRequestDto;
import com.gestion_employe.backend.dto.Response.MouvementStockResponseDto;
import com.gestion_employe.backend.dto.Response.StockProduitResponseDto;
import com.gestion_employe.backend.entity.EmployeEntity;
import com.gestion_employe.backend.entity.LigneReceptionAchatEntity;
import com.gestion_employe.backend.Enum.OrigineMouvement;
import com.gestion_employe.backend.Enum.TypeMouvement;

import java.util.List;

public interface MouvementStockService {

    /** Utilisé par les autres modules (validation de réception, etc.). */
    MouvementStockResponseDto enregistrer(Long idProduit, TypeMouvement type, int quantite,
                                          OrigineMouvement origine,
                                          LigneReceptionAchatEntity ligneReception,
                                          EmployeEntity responsable, String motif);

    /** Correction manuelle depuis l'API. */
    MouvementStockResponseDto creerCorrection(MouvementStockRequestDto dto);

    List<MouvementStockResponseDto> lister();

    List<MouvementStockResponseDto> listerParProduit(Long idProduit);

    StockProduitResponseDto stockProduit(Long idProduit);
}