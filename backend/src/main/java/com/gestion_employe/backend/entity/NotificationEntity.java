package com.gestion_employe.backend.entity;

import com.gestion_employe.backend.Enum.NiveauNotification;
import com.gestion_employe.backend.Enum.StatutNotification;
import com.gestion_employe.backend.Enum.TypeEntiteNotification;
import com.gestion_employe.backend.Enum.TypeNotification;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "notification")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class NotificationEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idNotification;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TypeNotification typeNotification;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private NiveauNotification niveau;

    @Column(nullable = false, length = 200)
    private String titre;

    @Column(nullable = false, length = 1000)
    private String message;

    @Column(nullable = false)
    private LocalDateTime dateCreation;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatutNotification statut = StatutNotification.NON_LUE;

    private LocalDateTime dateLecture;

    private LocalDateTime dateResolution;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TypeEntiteNotification typeEntite;

    /** Identifiant de l'élément concerné (id de l'achat, id de la ligne de réception...). */
    @Column(nullable = false)
    private Long idEntite;
}