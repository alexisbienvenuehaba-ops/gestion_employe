package com.gestion_employe.backend.entity;

import com.gestion_employe.backend.Enum.StatutReception;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "reception_achat")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ReceptionAchatEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idReceptionAchat;

    @Column(nullable = false)
    private LocalDate dateReception;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatutReception statut = StatutReception.BROUILLON;

    @Column(length = 1000)
    private String observation;

    /** Renseignée uniquement à la validation. */
    private LocalDateTime dateValidation;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "achat_id", nullable = false)
    private AchatEntity achatEntity;

    /** Null avant validation. */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "responsable_id")
    private EmployeEntity responsableEntity;

    /** Verrouillage optimiste : empêche deux validations simultanées. */
    @Version
    private Long version;
}