package com.gestion_employe.backend.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(name = "produit")
@NoArgsConstructor
@AllArgsConstructor
@Data
public class ProduitEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idProduit;

    private String nom;

    private Integer quantiteStock;

    private BigDecimal prix;

    private boolean deleted = false;

    @ManyToOne
    @JoinColumn(name = "fournisseur_id")
    private FournisseurEntity fournisseur;
}