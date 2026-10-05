package com.gestion_employe.backend.controler;

import com.gestion_employe.backend.dto.Request.FactureRequestDto;
import com.gestion_employe.backend.dto.Response.FactureResponseDto;
import com.gestion_employe.backend.service.FactureService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/facture")
public class FactureController {
    private final FactureService factureService;

    public FactureController(FactureService factureService) {
        this.factureService = factureService;
    }
    @GetMapping
    public List<FactureResponseDto> getAll() {
        return factureService.getAll();
    }

    @GetMapping("/{id}")
        public FactureResponseDto getById(@PathVariable Long id) {
        return factureService.getById(id);
    }
    @PostMapping
    public ResponseEntity<FactureResponseDto> create(@Valid @RequestBody FactureRequestDto request) {
        FactureResponseDto created = factureService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }
    @PutMapping("/{id}")
    public FactureResponseDto update(@PathVariable Long id, @Valid @RequestBody FactureRequestDto request) {
        return factureService.update(id, request);
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        factureService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
