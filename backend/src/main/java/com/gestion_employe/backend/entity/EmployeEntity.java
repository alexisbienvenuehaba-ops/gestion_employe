package com.gestion_employe.backend.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "employe")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmployeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idEmploye;

    private String nom;
    private String prenom;
    private String email;
    private String telephone;
    private String poste;


    private boolean deleted = false;
}
