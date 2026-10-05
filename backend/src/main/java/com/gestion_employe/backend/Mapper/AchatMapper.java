package com.gestion_employe.backend.Mapper;

import com.gestion_employe.backend.dto.Request.AchatRequestDto;
import com.gestion_employe.backend.dto.Response.AchatResponseDto;
import com.gestion_employe.backend.entity.AchatEntity;
import com.gestion_employe.backend.entity.FournisseurEntity;
import org.springframework.stereotype.Component;

@Component
public class AchatMapper {

    public AchatEntity toEntity(
            AchatRequestDto dto,
            FournisseurEntity fournisseur
    ) {
        AchatEntity achat = new AchatEntity();

        achat.setDateAchat(dto.getDateAchat());
        achat.setFournisseurEntity(fournisseur);
        achat.setTypePaiement(dto.getTypePaiement());
        achat.setDateEcheance(dto.getDateEcheance());

        achat.setMontantPaye(
                dto.getMontantPaye() != null
                        ? dto.getMontantPaye()
                        : java.math.BigDecimal.ZERO
        );

        achat.setTotal(java.math.BigDecimal.ZERO);
        achat.setResteAPayer(java.math.BigDecimal.ZERO);

        achat.setStatut("EN_COURS");

        return achat;
    }

    public AchatResponseDto toResponse(AchatEntity achat) {

        return new AchatResponseDto(
                achat.getIdAchat(),
                achat.getDateAchat(),
                achat.getTotal(),
                achat.getMontantPaye(),
                achat.getResteAPayer(),
                achat.getTypePaiement(),
                achat.getDateEcheance(),
                achat.getStatut(),
                achat.getFournisseurEntity().getIdFournisseur(),
                achat.getFournisseurEntity().getNom()
        );
    }
}