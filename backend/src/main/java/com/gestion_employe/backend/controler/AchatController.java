package com.gestion_employe.backend.controler;

import com.gestion_employe.backend.dto.Request.AchatRequestDto;
import com.gestion_employe.backend.dto.Response.AchatResponseDto;
import com.gestion_employe.backend.service.AchatService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/achat")
@RequiredArgsConstructor
public class AchatController {

    private final AchatService achatService;

    @PostMapping
    public ResponseEntity<AchatResponseDto> add(
            @Valid @RequestBody AchatRequestDto dto
    ) {
        return ResponseEntity.ok(
                achatService.add(dto)
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<AchatResponseDto> update(
            @PathVariable Long id,
            @Valid @RequestBody AchatRequestDto dto
    ) {
        return ResponseEntity.ok(
                achatService.update(id, dto)
        );
    }

    @GetMapping
    public ResponseEntity<List<AchatResponseDto>> getAll() {
        return ResponseEntity.ok(
                achatService.getAll()
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<AchatResponseDto> getById(
            @PathVariable Long id
    ) {
        return ResponseEntity.ok(
                achatService.getById(id)
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @PathVariable Long id
    ) {
        achatService.delete(id);
        return ResponseEntity.noContent().build();
    }
}

