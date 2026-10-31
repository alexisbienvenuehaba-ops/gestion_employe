package com.gestion_employe.backend.dto.Response;

import com.gestion_employe.backend.Enum.OrigineMouvement;
import com.gestion_employe.backend.Enum.TypeMouvement;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MouvementStockResponseDto {

    private Long idMouvementStock;
    private Long idProduit;
    private String nomProduit;
    private TypeMouvement typeMouvement;
    private Integer quantite;
    private LocalDateTime dateMouvement;
    private OrigineMouvement origine;
    private Long idLigneReception;
    private Long idResponsable;
    private String motif;
}