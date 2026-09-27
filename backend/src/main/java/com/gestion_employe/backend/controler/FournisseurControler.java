package com.gestion_employe.backend.controler;

import com.gestion_employe.backend.dto.Request.EmployeRequestDto;
import com.gestion_employe.backend.dto.Request.FournisseurRequestDto;
import com.gestion_employe.backend.dto.Response.EmployeResponseDto;
import com.gestion_employe.backend.dto.Response.FournisseurResponseDto;
import com.gestion_employe.backend.service.FournisseurService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/fournisseur")
public class FournisseurControler {
    private final FournisseurService fournisseurService;

    public FournisseurControler(FournisseurService fournisseurService) {
        this.fournisseurService = fournisseurService;
    }
    @GetMapping()
    public List<FournisseurResponseDto> getAll() {
        return fournisseurService.getAll();
    }

    @GetMapping("/{id}")
    public FournisseurResponseDto getById(@PathVariable Long id) {
        return fournisseurService.getById(id);
    }

    @PostMapping
    public ResponseEntity<FournisseurResponseDto> create(@Valid @RequestBody FournisseurRequestDto request) {
        FournisseurResponseDto created = fournisseurService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }
    @PutMapping("/{id}")
    public FournisseurResponseDto update(@PathVariable Long id, @Valid @RequestBody FournisseurRequestDto request) {
        return fournisseurService.update(id, request);

    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        fournisseurService.delete(id);
        return ResponseEntity.noContent().build();
    }

}
