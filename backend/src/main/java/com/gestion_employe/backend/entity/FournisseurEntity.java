package com.gestion_employe.backend.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "fournisseur")
@NoArgsConstructor
@AllArgsConstructor
@Data
public class FournisseurEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idFournisseur;
    private String nom;
    private String prenom;
    private String telephone;

    private boolean deleted = false;


}
