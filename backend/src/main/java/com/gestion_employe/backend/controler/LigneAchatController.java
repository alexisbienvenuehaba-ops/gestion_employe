package com.gestion_employe.backend.controler;

import com.gestion_employe.backend.dto.Request.LigneAchatRequestDto;
import com.gestion_employe.backend.dto.Response.LigneAchatResponseDto;
import com.gestion_employe.backend.service.LigneAchatService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/ligne-achat")
@RequiredArgsConstructor
public class LigneAchatController {

    private final LigneAchatService ligneAchatService;

    @PostMapping
    public ResponseEntity<LigneAchatResponseDto> add(
            @Valid @RequestBody LigneAchatRequestDto dto
    ) {

        return ResponseEntity.ok(
                ligneAchatService.add(dto)
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<LigneAchatResponseDto> update(
            @PathVariable Long id,
            @Valid @RequestBody LigneAchatRequestDto dto
    ) {

        return ResponseEntity.ok(
                ligneAchatService.update(id, dto)
        );
    }

    @GetMapping
    public ResponseEntity<List<LigneAchatResponseDto>> getAll() {

        return ResponseEntity.ok(
                ligneAchatService.getAll()
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<LigneAchatResponseDto> getById(
            @PathVariable Long id
    ) {

        return ResponseEntity.ok(
                ligneAchatService.getById(id)
        );
    }

    @GetMapping("/achat/{idAchat}")
    public ResponseEntity<List<LigneAchatResponseDto>> getByAchat(
            @PathVariable Long idAchat
    ) {

        return ResponseEntity.ok(
                ligneAchatService.getByAchat(idAchat)
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @PathVariable Long id
    ) {

        ligneAchatService.delete(id);

        return ResponseEntity.noContent().build();
    }
}

