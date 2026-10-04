package com.gestion_employe.backend.service.serviceImpl;

import com.gestion_employe.backend.Mapper.FactureMapper;
import com.gestion_employe.backend.dto.Request.FactureRequestDto;
import com.gestion_employe.backend.dto.Response.FactureResponseDto;
import com.gestion_employe.backend.entity.*;
import com.gestion_employe.backend.repository.*;
import com.gestion_employe.backend.service.FactureService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service

public class FactureServiceImpl implements FactureService {

    private final ClientRepository clientRepository;
    private final EmployeRepository employeRepository;
    private final FactureRepository factureRepository;

    private final FactureMapper factureMapper;
    public FactureServiceImpl(ClientRepository clientRepository, EmployeRepository employeRepository, FactureRepository factureRepository, FactureMapper factureMapper) {

        this.clientRepository = clientRepository;
        this.employeRepository = employeRepository;
        this.factureRepository = factureRepository;
        this.factureMapper = factureMapper;
    }

    @Override
    public List<FactureResponseDto> getAll() {
        return factureRepository.findAllByDeletedFalse()
                .stream()
                .map(factureMapper::toResponse)
                .toList();
    }

    @Override
    public FactureResponseDto getById(Long id) {
        FactureEntity factureEntity = factureRepository.findByIdFactureAndDeletedFalse(id)
                .orElseThrow(() -> new RuntimeException("Facture introuvable avec l'id " + id));
        return factureMapper.toResponse(factureEntity);
    }

    @Override
    public FactureResponseDto create(FactureRequestDto request) {

        FactureEntity factureEntity = factureMapper.toEntity(request);

        FactureEntity saved = factureRepository.save(factureEntity);

        saved.setNumeroFacture(
                String.format("FAC-%d-%04d",
                        saved.getDateFacture().getYear(),
                        saved.getIdFacture())
        );

        saved = factureRepository.save(saved);

        return factureMapper.toResponse(saved);
    }

    @Override
    public FactureResponseDto update(Long id, FactureRequestDto request) {
        FactureEntity factureEntity = factureRepository.findByIdFactureAndDeletedFalse(id)
                .orElseThrow(() -> new RuntimeException("Facture introuvable avec l'id " + id));

        factureEntity.setStatut(request.getStatut());
        factureEntity.setDateFacture(request.getDateFacture());


        ClientEntity clientEntity = clientRepository
                .findByIdAndDeletedFalse(request.getId())
                .orElseThrow(() -> new RuntimeException(
                        "Client introuvable avec l'id " + request.getId()));
        clientEntity.setNom(clientEntity.getNom());

        EmployeEntity employeEntity = employeRepository
                .findByIdEmployeAndDeletedFalse(request.getId())
                .orElseThrow(() -> new RuntimeException(
                        "Employe introuvable avec l'id " + request.getId()));
        employeEntity.setNom(employeEntity.getNom());


        FactureEntity updated = factureRepository.save(factureEntity);
        return factureMapper.toResponse(updated);
    }

    @Override
    public void delete(Long id) {
        FactureEntity factureEntity = factureRepository.findByIdFactureAndDeletedFalse(id)
                .orElseThrow(() -> new RuntimeException("Facture introuvable avec l'id " + id));
        factureEntity.setDeleted(true);
        factureRepository.save(factureEntity);
    }
}
