package com.gestion_employe.backend.dto.Request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class AchatRequestDto {

    @NotNull(message = "La date de l'achat est obligatoire")
    private LocalDate dateAchat;

    @NotNull(message = "Le fournisseur est obligatoire")
    private Long idFournisseur;

    @NotNull(message = "Le type de paiement est obligatoire")
    private String typePaiement;

    private LocalDate dateEcheance;

    private BigDecimal montantPaye;
}