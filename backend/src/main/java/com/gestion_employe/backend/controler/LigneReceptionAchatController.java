package com.gestion_employe.backend.controler;

import com.gestion_employe.backend.dto.Request.LigneReceptionAchatRequestDto;
import com.gestion_employe.backend.dto.Response.LigneReceptionAchatResponseDto;
import com.gestion_employe.backend.service.LigneReceptionAchatService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/receptions-achat")
@RequiredArgsConstructor
public class LigneReceptionAchatController {

    private final LigneReceptionAchatService ligneService;

    @PostMapping("/{idReception}/lignes")
    @ResponseStatus(HttpStatus.CREATED)
    public LigneReceptionAchatResponseDto ajouter(@PathVariable Long idReception,
                                                  @Valid @RequestBody LigneReceptionAchatRequestDto dto) {
        return ligneService.ajouter(idReception, dto);
    }

    @GetMapping("/{idReception}/lignes")
    public List<LigneReceptionAchatResponseDto> lister(@PathVariable Long idReception) {
        return ligneService.listerParReception(idReception);
    }

    @PutMapping("/lignes/{idLigne}")
    public LigneReceptionAchatResponseDto modifier(@PathVariable Long idLigne,
                                                   @Valid @RequestBody LigneReceptionAchatRequestDto dto) {
        return ligneService.modifier(idLigne, dto);
    }

    @DeleteMapping("/lignes/{idLigne}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void supprimer(@PathVariable Long idLigne) {
        ligneService.supprimer(idLigne);
    }
}