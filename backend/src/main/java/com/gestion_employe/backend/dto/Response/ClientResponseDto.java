package com.gestion_employe.backend.dto.Response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor

public class ClientResponseDto {

    private Long id;
    private String nom;
    private String Prenom;
    private String email;
    private String telephone;
    private String entreprise;
}
