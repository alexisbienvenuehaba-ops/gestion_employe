package com.gestion_employe.backend.dto.Response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LigneFactureResponseDto {

    private Long idLigneFacture;
    private Integer quantite;
    private BigDecimal prixUnitaire;

    private Long idFacture;
    private String numeroFacture;
    private String nomClient;

    private Long idProduit;
    private String nomProduit;
}