package com.gestion_employe.backend.dto.Response;

    import com.gestion_employe.backend.Enum.StatutPaiementAchat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SoldeAchatResponseDto {

    private Long idAchat;
    private BigDecimal totalAchat;
    /** Somme des paiements validés. */
    private BigDecimal totalPaye;
    private BigDecimal resteAPayer;
    private StatutPaiementAchat statutPaiement;
    private LocalDate dateEcheance;
}