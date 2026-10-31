package com.gestion_employe.backend.dto.Response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StockProduitResponseDto {

    private Long idProduit;
    private String nomProduit;
    /** Valeur courante stockée dans le produit. */
    private Integer quantiteStock;
    /** Somme du journal (entrées - sorties), pour vérification. */
    private Long stockCalcule;
}