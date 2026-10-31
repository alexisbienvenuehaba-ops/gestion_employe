package com.gestion_employe.backend.controler;

import com.gestion_employe.backend.dto.Request.MouvementStockRequestDto;
import com.gestion_employe.backend.dto.Response.MouvementStockResponseDto;
import com.gestion_employe.backend.dto.Response.StockProduitResponseDto;
import com.gestion_employe.backend.service.MouvementStockService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/mouvements-stock")
@RequiredArgsConstructor
public class MouvementStockController {

    private final MouvementStockService mouvementStockService;

    @GetMapping
    public List<MouvementStockResponseDto> lister() {
        return mouvementStockService.lister();
    }

    @GetMapping("/produit/{idProduit}")
    public List<MouvementStockResponseDto> listerParProduit(@PathVariable Long idProduit) {
        return mouvementStockService.listerParProduit(idProduit);
    }

    @GetMapping("/produit/{idProduit}/stock")
    public StockProduitResponseDto stockProduit(@PathVariable Long idProduit) {
        return mouvementStockService.stockProduit(idProduit);
    }

    /** Correction manuelle (origine forcée à CORRECTION). */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public MouvementStockResponseDto creerCorrection(@Valid @RequestBody MouvementStockRequestDto dto) {
        return mouvementStockService.creerCorrection(dto);
    }
}