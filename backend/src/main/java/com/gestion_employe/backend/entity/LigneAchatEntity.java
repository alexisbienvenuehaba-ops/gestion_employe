package com.gestion_employe.backend.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

@Entity
@Table(name = "ligne_achat")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LigneAchatEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idLigneAchat;

    @NotNull
    private Integer quantite;

    @NotNull
    private BigDecimal prixUnitaire;

    @ManyToOne
    @JoinColumn(name = "achat_id", nullable = false)
    private AchatEntity achatEntity;

    @ManyToOne
    @JoinColumn(name = "produit_id", nullable = false)
    private ProduitEntity produitEntity;
}