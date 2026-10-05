package com.gestion_employe.backend.controler;

import com.gestion_employe.backend.dto.Request.EmployeRequestDto;
import com.gestion_employe.backend.dto.Response.EmployeResponseDto;
import com.gestion_employe.backend.service.EmployeService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/employe")
public class EmployeController {

    private final EmployeService employeService;

    public EmployeController(EmployeService employeService) {
        this.employeService = employeService;
    }
    @GetMapping()
    public List<EmployeResponseDto> getAll() {
        return employeService.getAll();
    }

    @GetMapping("/{id}")
    public EmployeResponseDto getById(@PathVariable Long id) {
        return employeService.getById(id);
    }
    @PostMapping
    public ResponseEntity<EmployeResponseDto> create(@Valid @RequestBody EmployeRequestDto request) {
        EmployeResponseDto created = employeService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }
    @PutMapping("/{id}")
    public EmployeResponseDto update(@PathVariable Long id, @Valid @RequestBody EmployeRequestDto request) {
        return employeService.update(id, request);
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        employeService.delete(id);
        return ResponseEntity.noContent().build();
    }

    }

