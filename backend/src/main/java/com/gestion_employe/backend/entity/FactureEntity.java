package com.gestion_employe.backend.entity;

import jakarta.persistence.*;
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
    private String statut;
    private LocalDate dateFacture;
    @ManyToOne
    @JoinColumn(name = "employe_id")
    private EmployeEntity employeEntity;

    @ManyToOne
    @JoinColumn(name = "client_id")
    private ClientEntity clientEntity;

}
