package com.gestion_employe.backend.dto.Response;

import com.gestion_employe.backend.Enum.TypeAction;
import com.gestion_employe.backend.Enum.TypeEntiteHistorique;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class HistoriqueActionResponseDto {

    private Long idHistoriqueAction;
    private LocalDateTime dateAction;
    private TypeAction typeAction;
    private TypeEntiteHistorique typeEntite;
    private Long idEntite;
    private String description;
    private Long idEmploye;
    private String nomEmploye;
    private String prenomEmploye;
}