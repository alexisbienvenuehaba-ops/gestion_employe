package com.gestion_employe.backend.dto.Request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class FactureRequestDto {
    private String statut;
    @NotNull(message = "La date de facturation est obligatoire")
    private LocalDate dateFacture;
    private Long id;
    @NotNull(message = "Le Nom de l'employe qui à facturé est obligatoire")
    private Long idEmploye;

}
