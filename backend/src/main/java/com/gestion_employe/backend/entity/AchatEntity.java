package com.gestion_employe.backend.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "achat")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AchatEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idAchat;

    private LocalDate dateAchat;

    private BigDecimal total;

    private BigDecimal montantPaye;

    private BigDecimal resteAPayer;

    private String typePaiement;

    private LocalDate dateEcheance;

    private String statut;

    @ManyToOne
    @JoinColumn(name = "fournisseur_id")
    private FournisseurEntity fournisseurEntity;

    private boolean deleted = false;
}