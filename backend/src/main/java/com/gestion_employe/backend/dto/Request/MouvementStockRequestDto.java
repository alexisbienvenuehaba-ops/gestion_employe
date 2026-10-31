package com.gestion_employe.backend.dto.Request;

import com.gestion_employe.backend.Enum.TypeMouvement;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MouvementStockRequestDto {

    @NotNull
    private Long idProduit;

    @NotNull
    private TypeMouvement typeMouvement;

    @NotNull
    @Min(1)
    private Integer quantite;

    @NotBlank
    private String motif;

    @NotNull
    private Long idResponsable;
}