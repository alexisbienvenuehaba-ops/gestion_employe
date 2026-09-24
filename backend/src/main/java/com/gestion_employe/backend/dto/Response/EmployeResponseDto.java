package com.gestion_employe.backend.dto.Response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmployeResponseDto {
    private Long idEmploye;

    private String nom;
    private String prenom;
    private String email;
    private String telephone;
    private String poste;

}
