package com.gestion_employe.backend.dto.Response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AchatResponseDto {

    private Long idAchat;

    private LocalDate dateAchat;

    private BigDecimal total;

    private BigDecimal montantPaye;

    private BigDecimal resteAPayer;

    private String typePaiement;

    private LocalDate dateEcheance;

    private String statut;

    private Long idFournisseur;

    private String nomFournisseur;
}