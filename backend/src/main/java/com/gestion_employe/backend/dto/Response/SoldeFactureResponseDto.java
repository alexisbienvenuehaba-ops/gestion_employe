package com.gestion_employe.backend.dto.Response;

import com.gestion_employe.backend.Enum.StatutPaiementFacture;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SoldeFactureResponseDto {

    private Long idFacture;
    private String numeroFacture;
    private BigDecimal totalFacture;
    /** Somme des paiements validés. */
    private BigDecimal totalPaye;
    private BigDecimal resteAPayer;
    private StatutPaiementFacture statutPaiement;
}