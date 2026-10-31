package com.gestion_employe.backend.controler;

import com.gestion_employe.backend.dto.Request.AnnulerPaiementRequestDto;
import com.gestion_employe.backend.dto.Request.PaiementClientRequestDto;
import com.gestion_employe.backend.dto.Request.ValiderPaiementRequestDto;
import com.gestion_employe.backend.dto.Response.PaiementClientResponseDto;
import com.gestion_employe.backend.dto.Response.SoldeFactureResponseDto;
import com.gestion_employe.backend.service.PaiementClientService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/paiements-client")
@RequiredArgsConstructor
public class PaiementClientController {

    private final PaiementClientService paiementService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public PaiementClientResponseDto creer(@Valid @RequestBody PaiementClientRequestDto dto) {
        return paiementService.creer(dto);
    }

    @GetMapping
    public List<PaiementClientResponseDto> lister() {
        return paiementService.lister();
    }

    @GetMapping("/{id}")
    public PaiementClientResponseDto trouverParId(@PathVariable Long id) {
        return paiementService.trouverParId(id);
    }

    @GetMapping("/facture/{idFacture}")
    public List<PaiementClientResponseDto> listerParFacture(@PathVariable Long idFacture) {
        return paiementService.listerParFacture(idFacture);
    }

    @GetMapping("/facture/{idFacture}/solde")
    public SoldeFactureResponseDto soldeFacture(@PathVariable Long idFacture) {
        return paiementService.soldeFacture(idFacture);
    }

    @PutMapping("/{id}/valider")
    public PaiementClientResponseDto valider(@PathVariable Long id,
                                             @Valid @RequestBody ValiderPaiementRequestDto dto) {
        return paiementService.valider(id, dto);
    }

    @PutMapping("/{id}/annuler")
    public PaiementClientResponseDto annuler(@PathVariable Long id,
                                             @Valid @RequestBody AnnulerPaiementRequestDto dto) {
        return paiementService.annuler(id, dto);
    }
}