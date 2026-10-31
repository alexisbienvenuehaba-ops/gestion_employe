package com.gestion_employe.backend.dto.Request;

import com.gestion_employe.backend.Enum.DecisionRefus;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DecisionRefusRequestDto {

    /** RETOUR_FOURNISSEUR ou ACCEPTEE_EXCEPTIONNELLE. */
    @NotNull
    private DecisionRefus decision;

    @NotNull
    private Long idResponsable;

    /** Obligatoire si la décision est ACCEPTEE_EXCEPTIONNELLE. */
    private String motif;
}