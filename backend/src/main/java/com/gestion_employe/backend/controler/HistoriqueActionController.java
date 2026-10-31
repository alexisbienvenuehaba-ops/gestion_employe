package com.gestion_employe.backend.controler;

import com.gestion_employe.backend.dto.Response.HistoriqueActionResponseDto;
import com.gestion_employe.backend.Enum.TypeAction;
import com.gestion_employe.backend.Enum.TypeEntiteHistorique;
import com.gestion_employe.backend.service.HistoriqueActionService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.gestion_employe.backend.Enum.TypeAction;
import com.gestion_employe.backend.Enum.TypeEntiteHistorique;
import com.gestion_employe.backend.service.HistoriqueActionService;

import java.time.LocalDate;

/** Consultation seulement : aucun endpoint ne crée, modifie ou supprime l'historique. */
@RestController
@RequestMapping("/api/historique-actions")
@RequiredArgsConstructor
public class HistoriqueActionController {

    private final HistoriqueActionService historiqueService;
    private final HistoriqueActionService historiqueActionService;
    @GetMapping
    public Page<HistoriqueActionResponseDto> rechercher(
            @RequestParam(required = false) Long idEmploye,
            @RequestParam(required = false) TypeAction typeAction,
            @RequestParam(required = false) TypeEntiteHistorique typeEntite,
            @RequestParam(required = false) Long idEntite,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateDebut,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateFin,
            @PageableDefault(size = 20, sort = "dateAction", direction = Sort.Direction.DESC) Pageable pageable) {
        return historiqueService.rechercher(idEmploye, typeAction, typeEntite, idEntite,
                dateDebut, dateFin, pageable);


    }

}