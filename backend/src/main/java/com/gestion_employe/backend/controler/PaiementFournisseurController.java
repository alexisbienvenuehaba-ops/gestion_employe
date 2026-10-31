package com.gestion_employe.backend.controler;

import com.gestion_employe.backend.dto.Request.AnnulerPaiementRequestDto;
import com.gestion_employe.backend.dto.Request.PaiementFournisseurRequestDto;
import com.gestion_employe.backend.dto.Request.ValiderPaiementRequestDto;
import com.gestion_employe.backend.dto.Response.PaiementFournisseurResponseDto;
import com.gestion_employe.backend.dto.Response.SoldeAchatResponseDto;
import com.gestion_employe.backend.service.PaiementFournisseurService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/paiements-fournisseur")
@RequiredArgsConstructor
public class PaiementFournisseurController {

    private final PaiementFournisseurService paiementService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public PaiementFournisseurResponseDto creer(@Valid @RequestBody PaiementFournisseurRequestDto dto) {
        return paiementService.creer(dto);
    }

    @GetMapping
    public List<PaiementFournisseurResponseDto> lister() {
        return paiementService.lister();
    }

    @GetMapping("/{id}")
    public PaiementFournisseurResponseDto trouverParId(@PathVariable Long id) {
        return paiementService.trouverParId(id);
    }

    @GetMapping("/achat/{idAchat}")
    public List<PaiementFournisseurResponseDto> listerParAchat(@PathVariable Long idAchat) {
        return paiementService.listerParAchat(idAchat);
    }

    @GetMapping("/achat/{idAchat}/solde")
    public SoldeAchatResponseDto soldeAchat(@PathVariable Long idAchat) {
        return paiementService.soldeAchat(idAchat);
    }

    @PutMapping("/{id}/valider")
    public PaiementFournisseurResponseDto valider(@PathVariable Long id,
                                                  @Valid @RequestBody ValiderPaiementRequestDto dto) {
        return paiementService.valider(id, dto);
    }

    @PutMapping("/{id}/annuler")
    public PaiementFournisseurResponseDto annuler(@PathVariable Long id,
                                                  @Valid @RequestBody AnnulerPaiementRequestDto dto) {
        return paiementService.annuler(id, dto);
    }
}