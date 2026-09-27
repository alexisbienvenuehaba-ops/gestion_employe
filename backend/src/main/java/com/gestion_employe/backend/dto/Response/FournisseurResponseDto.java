package com.gestion_employe.backend.dto.Response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FournisseurResponseDto {
    private Long idFournisseur;
    private String nom;
    private String prenom;
    private String telephone;


}
