package com.gestion_employe.backend.entity;

import jakarta.persistence.*;
import com.gestion_employe.backend.Enum.StatutPaiementFacture;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Table(name = "facture")
@NoArgsConstructor
@AllArgsConstructor
@Data
public class FactureEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idFacture;

    private String numeroFacture;

    private String statut;

    private LocalDate dateFacture;

    @ManyToOne
    @JoinColumn(name = "employe_id")
    private EmployeEntity employeEntity;

    @ManyToOne
    @JoinColumn(name = "client_id")
    private ClientEntity clientEntity;

    private boolean deleted = false;

    @Enumerated(EnumType.STRING)
    private StatutPaiementFacture statutPaiement = StatutPaiementFacture.NON_PAYEE;
}