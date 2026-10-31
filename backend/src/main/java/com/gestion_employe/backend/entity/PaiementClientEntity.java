package com.gestion_employe.backend.entity;

import com.gestion_employe.backend.Enum.ModePaiement;
import com.gestion_employe.backend.Enum.StatutPaiement;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "paiement_client")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PaiementClientEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idPaiementClient;

    @Column(nullable = false)
    private LocalDate datePaiement;

    @Column(nullable = false, precision = 19, scale = 2)
    private BigDecimal montant;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ModePaiement modePaiement;

    /** Numéro de transaction mobile money, de chèque, de virement... (facultatif). */
    @Column(length = 100)
    private String reference;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatutPaiement statut = StatutPaiement.BROUILLON;

    @Column(length = 1000)
    private String observation;

    /** Renseignée uniquement à la validation. */
    private LocalDateTime dateValidation;

    private LocalDateTime dateAnnulation;

    @Column(length = 500)
    private String motifAnnulation;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "facture_id", nullable = false)
    private FactureEntity factureEntity;

    /** Responsable qui a validé le paiement. Null avant validation. */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "responsable_id")
    private EmployeEntity responsableEntity;

    /** Responsable qui a annulé le paiement. Null si non annulé. */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "responsable_annulation_id")
    private EmployeEntity responsableAnnulationEntity;

    /** Verrouillage optimiste. */
    @Version
    private Long version;
}