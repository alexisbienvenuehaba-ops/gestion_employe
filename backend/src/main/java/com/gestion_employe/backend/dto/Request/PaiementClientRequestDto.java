package com.gestion_employe.backend.dto.Request;

import com.gestion_employe.backend.Enum.ModePaiement;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaiementClientRequestDto {

    @NotNull
    private Long idFacture;

    @NotNull
    @Positive
    private BigDecimal montant;

    @NotNull
    private ModePaiement modePaiement;

    /** Si vide, la date du jour est utilisée. */
    private LocalDate datePaiement;

    private String reference;

    private String observation;
}