package com.gestion_employe.backend.dto.Response;

import com.gestion_employe.backend.Enum.NiveauNotification;
import com.gestion_employe.backend.Enum.StatutNotification;
import com.gestion_employe.backend.Enum.TypeEntiteNotification;
import com.gestion_employe.backend.Enum.TypeNotification;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NotificationResponseDto {

    private Long idNotification;
    private TypeNotification typeNotification;
    private NiveauNotification niveau;
    private String titre;
    private String message;
    private LocalDateTime dateCreation;
    private StatutNotification statut;
    private LocalDateTime dateLecture;
    private LocalDateTime dateResolution;
    private TypeEntiteNotification typeEntite;
    private Long idEntite;
}