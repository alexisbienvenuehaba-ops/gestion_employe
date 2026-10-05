package com.gestion_employe.backend.dto.Response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LigneAchatResponseDto {

    private Long idLigneAchat;

    private Integer quantite;

    private BigDecimal prixUnitaire;

    private Long idAchat;

    private Long idProduit;

    private String nomProduit;
}