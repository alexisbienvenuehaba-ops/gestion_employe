package com.gestion_employe.backend.service;

import com.gestion_employe.backend.dto.Response.HistoriqueActionResponseDto;
import com.gestion_employe.backend.entity.EmployeEntity;
import com.gestion_employe.backend.Enum.TypeAction;
import com.gestion_employe.backend.Enum.TypeEntiteHistorique;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;

public interface HistoriqueActionService {

    /** Appelé par les autres modules, dans la même transaction que l'action enregistrée. */
    void enregistrer(TypeAction typeAction, TypeEntiteHistorique typeEntite, Long idEntite,
                     EmployeEntity employe, String description);

    /** Recherche avec filtres facultatifs. */
    Page<HistoriqueActionResponseDto> rechercher(Long idEmploye, TypeAction typeAction,
                                                 TypeEntiteHistorique typeEntite, Long idEntite,
                                                 LocalDate dateDebut, LocalDate dateFin,
                                                 Pageable pageable);
}