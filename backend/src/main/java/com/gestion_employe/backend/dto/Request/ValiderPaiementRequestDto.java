package com.gestion_employe.backend.dto.Request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ValiderPaiementRequestDto {

    @NotNull
    private Long idResponsable;
}