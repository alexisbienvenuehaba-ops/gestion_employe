package com.gestion_employe.backend.dto.Response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProduitResponseDto {
    private Long idProduit;
    private String nom;
    private Integer quantiteStock;
    private BigDecimal prix;
    private Long idFournisseur;
    private String nomFournisseur;
}