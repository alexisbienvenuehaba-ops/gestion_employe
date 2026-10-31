package com.gestion_employe.backend.entity;

import com.gestion_employe.backend.Enum.OrigineMouvement;
import com.gestion_employe.backend.Enum.TypeMouvement;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "mouvement_stock")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MouvementStockEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idMouvementStock;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "produit_id", nullable = false)
    private ProduitEntity produitEntity;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TypeMouvement typeMouvement;

    /** Toujours positive ; le sens vient de typeMouvement. */
    @Column(nullable = false)
    private Integer quantite;

    @Column(nullable = false)
    private LocalDateTime dateMouvement;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private OrigineMouvement origine;

    /** Null pour les mouvements qui ne viennent pas d'une réception. */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ligne_reception_id")
    private LigneReceptionAchatEntity ligneReceptionAchatEntity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "responsable_id")
    private EmployeEntity responsableEntity;

    @Column(length = 500)
    private String motif;
}