package com.gestion_employe.backend.controler;

import com.gestion_employe.backend.dto.Request.FactureRequestDto;
import com.gestion_employe.backend.dto.Request.LigneFactureRequestDto;
import com.gestion_employe.backend.dto.Response.FactureResponseDto;
import com.gestion_employe.backend.dto.Response.LigneFactureResponseDto;
import com.gestion_employe.backend.service.LigneFactureService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/ligne_facture")
public class LigneFactureControler {
    private final LigneFactureService ligneFactureService;

    public LigneFactureControler(LigneFactureService ligneFactureService) {
        this.ligneFactureService = ligneFactureService;
    }
    @GetMapping
    public List<LigneFactureResponseDto> getAll() {
        return ligneFactureService.getAll();
    }

    @GetMapping("/{id}")
    public LigneFactureResponseDto getById(@PathVariable Long id) {
        return ligneFactureService.getById(id);
    }

    @PostMapping
    public ResponseEntity<LigneFactureResponseDto> create(@Valid @RequestBody LigneFactureRequestDto request) {
        LigneFactureResponseDto created = ligneFactureService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }
    @PutMapping("/{id}")
    public LigneFactureResponseDto update(@PathVariable Long id, @Valid @RequestBody LigneFactureRequestDto request) {
        return ligneFactureService.update(id, request);
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        ligneFactureService.delete(id);
        return ResponseEntity.noContent().build();
    }

}
