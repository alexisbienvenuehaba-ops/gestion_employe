package com.gestion_employe.backend.service.serviceImpl;

import com.gestion_employe.backend.dto.Response.HistoriqueActionResponseDto;
import com.gestion_employe.backend.entity.EmployeEntity;
import com.gestion_employe.backend.entity.HistoriqueActionEntity;
import com.gestion_employe.backend.Enum.TypeAction;
import com.gestion_employe.backend.Enum.TypeEntiteHistorique;
import com.gestion_employe.backend.Mapper.HistoriqueActionMapper;
import com.gestion_employe.backend.repository.HistoriqueActionRepository;
import com.gestion_employe.backend.service.HistoriqueActionService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class HistoriqueActionServiceImpl implements HistoriqueActionService {

    private static final int LONGUEUR_MAX_DESCRIPTION = 1000;

    private final HistoriqueActionRepository historiqueRepository;
    private final HistoriqueActionMapper mapper;

    @Override
    @Transactional
    public void enregistrer(TypeAction typeAction, TypeEntiteHistorique typeEntite, Long idEntite,
                            EmployeEntity employe, String description) {
        HistoriqueActionEntity historique = new HistoriqueActionEntity();
        historique.setDateAction(LocalDateTime.now());
        historique.setTypeAction(typeAction);
        historique.setTypeEntite(typeEntite);
        historique.setIdEntite(idEntite);
        historique.setEmployeEntity(employe);
        historique.setDescription(description.length() > LONGUEUR_MAX_DESCRIPTION
                ? description.substring(0, LONGUEUR_MAX_DESCRIPTION)
                : description);
        historiqueRepository.save(historique);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<HistoriqueActionResponseDto> rechercher(Long idEmploye, TypeAction typeAction,
                                                        TypeEntiteHistorique typeEntite, Long idEntite,
                                                        LocalDate dateDebut, LocalDate dateFin,
                                                        Pageable pageable) {
        Specification<HistoriqueActionEntity> spec = (root, query, cb) -> cb.conjunction();

        if (idEmploye != null) {
            spec = spec.and((root, query, cb) ->
                    cb.equal(root.get("employeEntity").get("idEmploye"), idEmploye));
        }
        if (typeAction != null) {
            spec = spec.and((root, query, cb) -> cb.equal(root.get("typeAction"), typeAction));
        }
        if (typeEntite != null) {
            spec = spec.and((root, query, cb) -> cb.equal(root.get("typeEntite"), typeEntite));
        }
        if (idEntite != null) {
            spec = spec.and((root, query, cb) -> cb.equal(root.get("idEntite"), idEntite));
        }
        if (dateDebut != null) {
            spec = spec.and((root, query, cb) ->
                    cb.greaterThanOrEqualTo(root.get("dateAction"), dateDebut.atStartOfDay()));
        }
        if (dateFin != null) {
            spec = spec.and((root, query, cb) ->
                    cb.lessThan(root.get("dateAction"), dateFin.plusDays(1).atStartOfDay()));
        }

        return historiqueRepository.findAll(spec, pageable).map(mapper::toResponseDto);
    }
}