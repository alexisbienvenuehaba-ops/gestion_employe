package com.gestion_employe.backend.dto.Request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;

@Data
public class FactureRequestDto {

    private String statut;

    @NotNull(message = "La date de facturation est obligatoire")
    private LocalDate dateFacture;

    @NotNull(message = "Le client est obligatoire")
    private Long id;

    @NotNull(message = "L'employé est obligatoire")
    private Long idEmploye;
}