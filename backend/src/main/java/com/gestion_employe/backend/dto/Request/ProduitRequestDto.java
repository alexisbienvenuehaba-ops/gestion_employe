package com.gestion_employe.backend.dto.Request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class ProduitRequestDto {

    @NotBlank(message = "Le nom du produit est obligatoire")
    private String nom;

    @NotNull(message = "La quantité en stock est obligatoire")
    @PositiveOrZero(message = "La quantité en stock ne peut pas être négative")
    private Integer quantiteStock;

    @NotNull(message = "Le prix est obligatoire")
    @Positive(message = "Le prix doit être supérieur à 0")
    private BigDecimal prix;

    @NotNull(message = "Le fournisseur est obligatoire")
    private Long idFournisseur;
}