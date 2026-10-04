package com.gestion_employe.backend.dto.Response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FactureResponseDto {

    private Long idFacture;
    private String numeroFacture;
    private String statut;
    private LocalDate dateFacture;

    private Long id;
    private String nomClient;

    private Long idEmploye;
    private String nomEmploye;
}