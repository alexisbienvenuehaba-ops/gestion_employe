package com.gestion_employe.backend.service.serviceImpl;

import com.gestion_employe.backend.Mapper.ClientMapper;
import com.gestion_employe.backend.dto.Request.ClientRequestDto;
import com.gestion_employe.backend.dto.Response.ClientResponseDto;
import com.gestion_employe.backend.entity.ClientEntity;
import com.gestion_employe.backend.repository.ClientRepository;
import com.gestion_employe.backend.service.ClientService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ClientServiceImpl implements ClientService {

    private final ClientRepository clientRepository;
    private final ClientMapper clientMapper;

    @Override
    public List<ClientResponseDto> getAll() {
        return clientRepository.findAllByDeletedFalse()
                .stream()
                .map(clientMapper::toResponse)
                .toList();
    }

    @Override
    public ClientResponseDto getById(Long id) {
        ClientEntity client = clientRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Client introuvable avec l'id " + id));
        return clientMapper.toResponse(client);
    }

    @Override
    public ClientResponseDto create(ClientRequestDto request) {
        ClientEntity client = clientMapper.toEntity(request);
        ClientEntity saved = clientRepository.save(client);
        return clientMapper.toResponse(saved);
    }

    @Override
    public ClientResponseDto update(Long id, ClientRequestDto request) {
        ClientEntity client = clientRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Client introuvable avec l'id " + id));

        client.setNom(request.getNom());
        client.setPrenom(request.getPrenom());
        client.setEmail(request.getEmail());
        client.setTelephone(request.getTelephone());
        client.setEntreprise(request.getEntreprise());

        ClientEntity updated = clientRepository.save(client);
        return clientMapper.toResponse(updated);
    }

    @Override
    public void delete(Long id) {
        ClientEntity client = clientRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Client introuvable avec l'id " + id));

        client.setDeleted(true);
        clientRepository.save(client);
    }
}