package com.gestion_employe.backend.service.serviceImpl;

import com.gestion_employe.backend.Mapper.FournisseurMapper;
import com.gestion_employe.backend.dto.Request.FournisseurRequestDto;
import com.gestion_employe.backend.dto.Response.FournisseurResponseDto;
import com.gestion_employe.backend.entity.EmployeEntity;
import com.gestion_employe.backend.entity.FournisseurEntity;
import com.gestion_employe.backend.repository.FournisseurRepository;
import com.gestion_employe.backend.service.FournisseurService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FournisseurServiceImpl implements FournisseurService {
    private final FournisseurRepository fournisseurRepository;

    private final FournisseurMapper fournisseurMapper;

    public FournisseurServiceImpl(FournisseurRepository fournisseurRepository, FournisseurMapper fournisseurMapper) {
        this.fournisseurRepository = fournisseurRepository;
        this.fournisseurMapper = fournisseurMapper;
    }


    @Override
    public List<FournisseurResponseDto> getAll() {
        return fournisseurRepository.findAllByDeletedFalse()
                .stream()
                .map(fournisseurMapper::toResponse)
                .toList();
    }

    @Override
    public FournisseurResponseDto getById(Long id) {
        FournisseurEntity fournisseurEntity = fournisseurRepository.findByIdFournisseurAndDeletedFalse(id)
                .orElseThrow(() -> new RuntimeException("Fournisseur introuvable avec l'id " + id));
        return fournisseurMapper.toResponse(fournisseurEntity);
    }

    @Override
    public FournisseurResponseDto create(FournisseurRequestDto request) {
        FournisseurEntity fournisseurEntity = fournisseurMapper.toEntity(request);
        FournisseurEntity saved = fournisseurRepository.save(fournisseurEntity);
        return fournisseurMapper.toResponse(saved);
    }

    @Override
    public FournisseurResponseDto update(Long id, FournisseurRequestDto request) {
        FournisseurEntity fournisseurEntity = fournisseurRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Fournisseur introuvable avec l'id " + id));

        fournisseurEntity.setNom(request.getNom());
        fournisseurEntity.setPrenom(request.getPrenom());
        fournisseurEntity.setTelephone(request.getTelephone());

        FournisseurEntity updated = fournisseurRepository.save(fournisseurEntity);
        return fournisseurMapper.toResponse(updated);
    }

    @Override
    public void delete(Long id) {
        FournisseurEntity fournisseurEntity = fournisseurRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Fournisseur introuvable avec l'id " + id));

        fournisseurEntity.setDeleted(true);
        fournisseurRepository.save(fournisseurEntity);

    }
}
