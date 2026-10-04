package com.gestion_employe.backend.dto.Request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class LigneFactureRequestDto {

    @NotNull(message = "La quantité est obligatoire")
    private Integer quantite;

    @NotNull(message = "Le prix unitaire est obligatoire")
    @PositiveOrZero(message = "Le prix unitaire ne doit pas être négatif")
    private BigDecimal prixUnitaire;

    @NotNull(message = "La facture est obligatoire")
    private Long idFacture;

    @NotNull(message = "Le produit est obligatoire")
    private Long idProduit;
}