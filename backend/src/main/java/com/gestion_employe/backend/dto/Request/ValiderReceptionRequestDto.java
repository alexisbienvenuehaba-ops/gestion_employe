package com.gestion_employe.backend.dto.Request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ValiderReceptionRequestDto {

    /** Responsable qui valide la réception. */
    @NotNull
    private Long idResponsable;
}