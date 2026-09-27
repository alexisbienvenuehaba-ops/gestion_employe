package com.gestion_employe.backend.controler;

import com.gestion_employe.backend.dto.Request.ProduitRequestDto;
import com.gestion_employe.backend.dto.Response.ProduitResponseDto;
import com.gestion_employe.backend.service.ProduitService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/produit")
public class ProduitControler {

    private final ProduitService produitService;

    public ProduitControler(ProduitService produitService) {
        this.produitService = produitService;
    }

    @GetMapping
    public List<ProduitResponseDto> getAll() {
        return produitService.getAll();
    }

    @GetMapping("/{id}")
    public ProduitResponseDto getById(@PathVariable Long id) {
        return produitService.getById(id);
    }

    @PostMapping
    public ResponseEntity<ProduitResponseDto> create(@Valid @RequestBody ProduitRequestDto request) {
        ProduitResponseDto created = produitService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/{id}")
    public ProduitResponseDto update(@PathVariable Long id, @Valid @RequestBody ProduitRequestDto request) {
        return produitService.update(id, request);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        produitService.delete(id);
        return ResponseEntity.noContent().build();
    }
}