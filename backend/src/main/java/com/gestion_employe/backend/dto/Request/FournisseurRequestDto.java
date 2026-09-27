package com.gestion_employe.backend.dto.Request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class FournisseurRequestDto {
    @NotBlank(message = "Le  nom est obligatoire")
    private String nom;
    @NotBlank(message = "Le prenom est obligatoire")
    private String prenom;
    @NotBlank(message = "Le contact est obligatoire")
    private String telephone;
}
