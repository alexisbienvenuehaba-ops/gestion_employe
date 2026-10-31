package com.gestion_employe.backend.dto.Request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LigneReceptionAchatRequestDto {

    /** Obligatoire à l'ajout ; ignoré à la modification. */
    private Long idLigneAchat;

    @NotNull
    @Min(0)
    private Integer quantiteAcceptee;

    @NotNull
    @Min(0)
    private Integer quantiteRefusee;

    private String motifRefus;
}