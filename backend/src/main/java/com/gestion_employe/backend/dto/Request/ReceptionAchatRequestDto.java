package com.gestion_employe.backend.dto.Request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReceptionAchatRequestDto {

    @NotNull
    private Long idAchat;

    /** Si vide, la date du jour est utilisée. */
    private LocalDate dateReception;

    private String observation;
}