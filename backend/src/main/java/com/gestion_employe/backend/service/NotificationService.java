package com.gestion_employe.backend.service;

import com.gestion_employe.backend.Enum.StatutNotification;
import com.gestion_employe.backend.dto.Response.NotificationResponseDto;

import java.util.List;

public interface NotificationService {

    /** Vérifie les échéances et les refus en attente : crée les nouvelles alertes, ferme celles qui n'ont plus lieu d'être. */
    void verifier();

    /** Sans statut : les notifications actives (non lues et lues). */
    List<NotificationResponseDto> lister(StatutNotification statut);

    long compterNonLues();

    NotificationResponseDto marquerCommeLue(Long id);

    void marquerToutesCommeLues();
}