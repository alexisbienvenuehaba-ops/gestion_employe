package com.gestion_employe.backend.controler;

import com.gestion_employe.backend.dto.Request.ClientRequestDto;
import com.gestion_employe.backend.dto.Response.ClientResponseDto;
import com.gestion_employe.backend.service.ClientService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/clients")

public class ClientControler {

    private final ClientService clientService;

    public ClientControler(ClientService clientService) {
        this.clientService = clientService;
    }

    @GetMapping()
    public List<ClientResponseDto> getAll() {
        return clientService.getAll();
    }

    @GetMapping("/{id}")
    public ClientResponseDto getById(@PathVariable Long id) {
        return clientService.getById(id);
    }

    @PostMapping
    public ResponseEntity<ClientResponseDto> create(@Valid @RequestBody ClientRequestDto request) {
        ClientResponseDto created = clientService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/{id}")
    public ClientResponseDto update(@PathVariable Long id, @Valid @RequestBody ClientRequestDto request) {
        return clientService.update(id, request);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        clientService.delete(id);
        return ResponseEntity.noContent().build();
    }
}