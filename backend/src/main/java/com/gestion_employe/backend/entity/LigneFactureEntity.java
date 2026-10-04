package com.gestion_employe.backend.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.time.LocalDate;

@Entity
@Table(name = "ligne_facture")
@NoArgsConstructor
@AllArgsConstructor
@Data
public class LigneFactureEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idLigneFacture;
    private Integer quantite;
    private BigDecimal prixUnitaire;

    @ManyToOne
    @JoinColumn(name = "facture_id")
    private FactureEntity factureEntity;

    @ManyToOne
    @JoinColumn(name = "produit_id")
    private ProduitEntity produitEntity;

    private boolean deleted = false;
}
