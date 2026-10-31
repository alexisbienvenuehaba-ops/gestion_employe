package com.gestion_employe.backend.entity;

import com.gestion_employe.backend.Enum.DecisionRefus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "ligne_reception_achat")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LigneReceptionAchatEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idLigneReception;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "reception_achat_id", nullable = false)
    private ReceptionAchatEntity receptionAchatEntity;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "ligne_achat_id", nullable = false)
    private LigneAchatEntity ligneAchatEntity;

    /** Toujours égale à quantiteAcceptee + quantiteRefusee (calculée par le service). */
    @Column(nullable = false)
    private Integer quantiteLivree;

    @Column(nullable = false)
    private Integer quantiteAcceptee;

    @Column(nullable = false)
    private Integer quantiteRefusee;

    /** Obligatoire si quantiteRefusee > 0. */
    @Column(length = 500)
    private String motifRefus;

    /** Null s'il n'y a aucun refus ; EN_ATTENTE dès qu'il y a un refus. */
    @Enumerated(EnumType.STRING)
    private DecisionRefus decisionRefus;
}