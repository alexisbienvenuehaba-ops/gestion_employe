package com.gestion_employe.backend.entity;

import com.gestion_employe.backend.Enum.TypeAction;
import com.gestion_employe.backend.Enum.TypeEntiteHistorique;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

/** Journal des actions : jamais modifié, jamais supprimé. */
@Entity
@Table(name = "historique_action")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class HistoriqueActionEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idHistoriqueAction;

    @Column(nullable = false, updatable = false)
    private LocalDateTime dateAction;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, updatable = false)
    private TypeAction typeAction;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, updatable = false)
    private TypeEntiteHistorique typeEntite;

    /** Identifiant de l'élément concerné (par exemple l'id du paiement). */
    @Column(nullable = false, updatable = false)
    private Long idEntite;

    @Column(nullable = false, updatable = false, length = 1000)
    private String description;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "employe_id", nullable = false, updatable = false)
    private EmployeEntity employeEntity;
}