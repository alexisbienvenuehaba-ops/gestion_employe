package com.gestion_employe.backend.service.serviceImpl;

import com.gestion_employe.backend.Mapper.EmployeMapper;
import com.gestion_employe.backend.dto.Request.EmployeRequestDto;
import com.gestion_employe.backend.dto.Response.EmployeResponseDto;
import com.gestion_employe.backend.entity.ClientEntity;
import com.gestion_employe.backend.entity.EmployeEntity;
import com.gestion_employe.backend.repository.EmployeRepository;
import com.gestion_employe.backend.service.EmployeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
@RequiredArgsConstructor
public class EmployeServiceImpl implements EmployeService {
    private final EmployeRepository employeRepository;
    private final EmployeMapper employeMapper;
    @Override
    public List<EmployeResponseDto> getAll() {
        return employeRepository.findAllByDeletedFalse()
                .stream()
                .map(employeMapper::toResponse)
                .toList();
    }

    @Override
    public EmployeResponseDto getById(Long id) {
        EmployeEntity employeEntity = employeRepository.findByIdEmployeAndDeletedFalse(id)
                .orElseThrow(() -> new RuntimeException("Employe introuvable avec l'id " + id));
        return employeMapper.toResponse(employeEntity);
    }

    @Override
    public EmployeResponseDto create(EmployeRequestDto request) {

        EmployeEntity employeEntity = employeMapper.toEntity(request);
        EmployeEntity saved = employeRepository.save(employeEntity);
        return employeMapper.toResponse(saved);
    }



    @Override
    public EmployeResponseDto update(Long id, EmployeRequestDto request) {
        EmployeEntity employeEntity = employeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Employe introuvable avec l'id " + id));

        employeEntity.setNom(request.getNom());
        employeEntity.setPrenom(request.getPrenom());
        employeEntity.setEmail(request.getEmail());
        employeEntity.setTelephone(request.getTelephone());
        employeEntity.setPoste(request.getPoste());

        EmployeEntity updated = employeRepository.save(employeEntity);
        return employeMapper.toResponse(updated);
    }

    @Override
    public void delete(Long id) {
        EmployeEntity employeEntity = employeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Employe introuvable avec l'id " + id));

        employeEntity.setDeleted(true);
        employeRepository.save(employeEntity);
    }
}
