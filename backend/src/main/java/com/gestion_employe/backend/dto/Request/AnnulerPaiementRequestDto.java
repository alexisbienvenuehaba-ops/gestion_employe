package com.gestion_employe.backend.dto.Request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AnnulerPaiementRequestDto {

    @NotNull
    private Long idResponsable;

    /** Facultatif pour un brouillon, obligatoire pour un paiement déjà validé. */
    private String motif;
}