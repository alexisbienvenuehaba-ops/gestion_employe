package com.gestion_employe.backend.dto.Request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class LigneAchatRequestDto {

    @NotNull(message = "La quantité est obligatoire")
    @Min(value = 1, message = "La quantité doit être supérieure à 0")
    private Integer quantite;

    @NotNull(message = "Le prix unitaire est obligatoire")
    @Min(value = 0, message = "Le prix ne peut pas être négatif")
    private BigDecimal prixUnitaire;

    @NotNull(message = "L'achat est obligatoire")
    private Long idAchat;

    @NotNull(message = "Le produit est obligatoire")
    private Long idProduit;
}