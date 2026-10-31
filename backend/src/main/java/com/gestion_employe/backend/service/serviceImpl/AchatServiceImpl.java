package com.gestion_employe.backend.service.serviceImpl;

import com.gestion_employe.backend.Mapper.AchatMapper;
import com.gestion_employe.backend.dto.Request.AchatRequestDto;
import com.gestion_employe.backend.dto.Response.AchatResponseDto;
import com.gestion_employe.backend.entity.AchatEntity;
import com.gestion_employe.backend.entity.FournisseurEntity;
import com.gestion_employe.backend.repository.AchatRepository;
import com.gestion_employe.backend.repository.FournisseurRepository;
import com.gestion_employe.backend.service.AchatService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AchatServiceImpl implements AchatService {

    private final AchatRepository achatRepository;
    private final FournisseurRepository fournisseurRepository;
    private final AchatMapper achatMapper;

    @Override
    public AchatResponseDto add(AchatRequestDto dto) {

        FournisseurEntity fournisseur =
                fournisseurRepository.findById(dto.getIdFournisseur())
                        .orElseThrow(() ->
                                new RuntimeException("Fournisseur introuvable"));

        AchatEntity achat =
                achatMapper.toEntity(dto, fournisseur);

        achat.setTotal(BigDecimal.ZERO);
        achat.setResteAPayer(BigDecimal.ZERO);

        AchatEntity saved =
                achatRepository.save(achat);

        return achatMapper.toResponse(saved);
    }

    @Override
    public AchatResponseDto update(Long id, AchatRequestDto dto) {

        AchatEntity achat =
                achatRepository.findById(id)
                        .orElseThrow(() ->
                                new RuntimeException("Achat introuvable"));

        FournisseurEntity fournisseur =
                fournisseurRepository.findById(dto.getIdFournisseur())
                        .orElseThrow(() ->
                                new RuntimeException("Fournisseur introuvable"));

        achat.setDateAchat(dto.getDateAchat());
        achat.setFournisseurEntity(fournisseur);
        achat.setTypePaiement(dto.getTypePaiement());
        achat.setDateEcheance(dto.getDateEcheance());

        AchatEntity updated =
                achatRepository.save(achat);

        return achatMapper.toResponse(updated);
    }

    @Override
    public AchatResponseDto getById(Long id) {

        AchatEntity achat =
                achatRepository.findById(id)
                        .orElseThrow(() ->
                                new RuntimeException("Achat introuvable"));

        return achatMapper.toResponse(achat);
    }

    @Override
    public List<AchatResponseDto> getAll() {

        return achatRepository.findAllByDeletedFalse()
                .stream()
                .map(achatMapper::toResponse)
                .toList();
    }

    @Override
    public void delete(Long id) {

        AchatEntity achat =
                achatRepository.findById(id)
                        .orElseThrow(() ->
                                new RuntimeException("Achat introuvable"));

        achat.setDeleted(true);

        achatRepository.save(achat);
    }
}