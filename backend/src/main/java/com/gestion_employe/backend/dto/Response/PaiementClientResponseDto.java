package com.gestion_employe.backend.dto.Response;

import com.gestion_employe.backend.Enum.ModePaiement;
import com.gestion_employe.backend.Enum.StatutPaiement;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaiementClientResponseDto {

    private Long idPaiementClient;
    private LocalDate datePaiement;
    private BigDecimal montant;
    private ModePaiement modePaiement;
    private String reference;
    private StatutPaiement statut;
    private String observation;
    private LocalDateTime dateValidation;
    private LocalDateTime dateAnnulation;
    private String motifAnnulation;
    private Long idFacture;
    private String numeroFacture;
    private Long idResponsable;
    private Long idResponsableAnnulation;
}