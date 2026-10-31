package com.gestion_employe.backend.controler;

import com.gestion_employe.backend.dto.Request.ReceptionAchatRequestDto;
import com.gestion_employe.backend.dto.Response.ReceptionAchatResponseDto;
import com.gestion_employe.backend.service.ReceptionAchatService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/receptions-achat")
@RequiredArgsConstructor
public class ReceptionAchatController {

    private final ReceptionAchatService receptionAchatService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ReceptionAchatResponseDto creer(@Valid @RequestBody ReceptionAchatRequestDto dto) {
        return receptionAchatService.creer(dto);
    }

    @GetMapping
    public List<ReceptionAchatResponseDto> lister() {
        return receptionAchatService.lister();
    }

    @GetMapping("/{id}")
    public ReceptionAchatResponseDto trouverParId(@PathVariable Long id) {
        return receptionAchatService.trouverParId(id);
    }

    @GetMapping("/achat/{idAchat}")
    public List<ReceptionAchatResponseDto> listerParAchat(@PathVariable Long idAchat) {
        return receptionAchatService.listerParAchat(idAchat);
    }

    @PutMapping("/{id}/annuler")
    public ReceptionAchatResponseDto annuler(@PathVariable Long id) {
        return receptionAchatService.annuler(id);
    }
}