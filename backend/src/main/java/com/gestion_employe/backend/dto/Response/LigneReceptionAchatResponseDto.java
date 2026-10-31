package com.gestion_employe.backend.dto.Response;

import com.gestion_employe.backend.Enum.DecisionRefus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LigneReceptionAchatResponseDto {

    private Long idLigneReception;
    private Long idReceptionAchat;
    private Long idLigneAchat;
    private Long idProduit;
    private String nomProduit;
    private Integer quantiteCommandee;
    private Integer quantiteLivree;
    private Integer quantiteAcceptee;
    private Integer quantiteRefusee;
    private String motifRefus;
    private DecisionRefus decisionRefus;
}