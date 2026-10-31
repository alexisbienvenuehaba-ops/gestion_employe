package com.gestion_employe.backend.dto.Response;

import com.gestion_employe.backend.Enum.StatutReception;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReceptionAchatResponseDto {

    private Long idReceptionAchat;
    private LocalDate dateReception;
    private StatutReception statut;
    private String observation;
    private LocalDateTime dateValidation;
    private Long idAchat;
    private Long idResponsable;
}