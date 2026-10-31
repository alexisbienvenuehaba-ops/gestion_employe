package com.gestion_employe.backend.service.serviceImpl;

import com.gestion_employe.backend.Enum.DecisionRefus;
import com.gestion_employe.backend.Enum.NiveauNotification;
import com.gestion_employe.backend.Enum.StatutNotification;
import com.gestion_employe.backend.Enum.StatutPaiement;
import com.gestion_employe.backend.Enum.StatutReception;
import com.gestion_employe.backend.Enum.TypeEntiteNotification;
import com.gestion_employe.backend.Enum.TypeNotification;
import com.gestion_employe.backend.Mapper.NotificationMapper;
import com.gestion_employe.backend.dto.Response.NotificationResponseDto;
import com.gestion_employe.backend.entity.AchatEntity;
import com.gestion_employe.backend.entity.LigneReceptionAchatEntity;
import com.gestion_employe.backend.entity.NotificationEntity;
import com.gestion_employe.backend.repository.NotificationRepository;
import com.gestion_employe.backend.repository.PaiementFournisseurRepository;
import com.gestion_employe.backend.service.NotificationService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {

    /** Une échéance est « proche » quand elle tombe dans ce nombre de jours ou moins. */
    private static final int JOURS_AVANT_ECHEANCE = 7;

    private static final List<TypeNotification> TYPES_ECHEANCE =
            List.of(TypeNotification.ECHEANCE_ACHAT_PROCHE, TypeNotification.ECHEANCE_ACHAT_DEPASSEE);

    private static final DateTimeFormatter FORMAT_DATE = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    private final NotificationRepository notificationRepository;
    private final PaiementFournisseurRepository paiementFournisseurRepository;
    private final NotificationMapper mapper;

    @PersistenceContext
    private EntityManager entityManager;

    // ------------------------------------------------------------------ Vérification

    @Override
    @Transactional
    public void verifier() {
        LocalDate aujourdhui = LocalDate.now();
        verifierEcheancesAchats(aujourdhui);
        verifierRefusEnAttente();
    }

    private void verifierEcheancesAchats(LocalDate aujourdhui) {
        LocalDate limite = aujourdhui.plusDays(JOURS_AVANT_ECHEANCE);

        // Achats dont l'échéance est proche ou dépassée.
        Map<Long, AchatEntity> achats = new LinkedHashMap<>();
        entityManager.createQuery("""
                        select a from AchatEntity a
                        where a.deleted = false
                          and a.dateEcheance is not null
                          and a.dateEcheance <= :limite
                        """, AchatEntity.class)
                .setParameter("limite", limite)
                .getResultList()
                .forEach(achat -> achats.put(achat.getIdAchat(), achat));

        // Achats qui ont déjà une alerte active : ils sont réévalués pour fermer l'alerte si besoin.
        for (NotificationEntity notification :
                notificationRepository.findByTypeNotificationInAndStatutNot(TYPES_ECHEANCE, StatutNotification.RESOLUE)) {
            if (!achats.containsKey(notification.getIdEntite())) {
                AchatEntity achat = entityManager.find(AchatEntity.class, notification.getIdEntite());
                if (achat != null) {
                    achats.put(achat.getIdAchat(), achat);
                } else {
                    resoudre(notification);
                }
            }
        }

        for (AchatEntity achat : achats.values()) {
            evaluerAchat(achat, aujourdhui, limite);
        }
    }

    private void evaluerAchat(AchatEntity achat, LocalDate aujourdhui, LocalDate limite) {
        TypeNotification souhaitee = null;
        BigDecimal reste = BigDecimal.ZERO;

        if (!achat.isDeleted() && achat.getDateEcheance() != null && achat.getTotal() != null) {
            BigDecimal paye = paiementFournisseurRepository.sommeParStatut(achat, StatutPaiement.VALIDE);
            reste = achat.getTotal().subtract(paye != null ? paye : BigDecimal.ZERO);

            if (reste.signum() > 0 && !achat.getDateEcheance().isAfter(limite)) {
                souhaitee = achat.getDateEcheance().isBefore(aujourdhui)
                        ? TypeNotification.ECHEANCE_ACHAT_DEPASSEE
                        : TypeNotification.ECHEANCE_ACHAT_PROCHE;
            }
        }

        // Ferme les alertes d'échéance qui ne correspondent plus à la situation.
        for (NotificationEntity notification : notificationRepository.findByTypeEntiteAndIdEntiteAndStatutNot(
                TypeEntiteNotification.ACHAT, achat.getIdAchat(), StatutNotification.RESOLUE)) {
            if (TYPES_ECHEANCE.contains(notification.getTypeNotification())
                    && notification.getTypeNotification() != souhaitee) {
                resoudre(notification);
            }
        }

        // Crée l'alerte souhaitée si elle n'existe pas déjà (pas de doublon).
        if (souhaitee != null && !notificationRepository.existsByTypeNotificationAndTypeEntiteAndIdEntiteAndStatutNot(
                souhaitee, TypeEntiteNotification.ACHAT, achat.getIdAchat(), StatutNotification.RESOLUE)) {

            String montant = reste.stripTrailingZeros().toPlainString();
            String date = achat.getDateEcheance().format(FORMAT_DATE);
            long jours = Math.abs(ChronoUnit.DAYS.between(aujourdhui, achat.getDateEcheance()));

            if (souhaitee == TypeNotification.ECHEANCE_ACHAT_DEPASSEE) {
                creer(souhaitee, NiveauNotification.URGENT, "Échéance d'achat dépassée",
                        "L'échéance de l'achat n°" + achat.getIdAchat() + " (" + date + ") est dépassée de "
                                + jours + " jour(s) : il reste " + montant + " à régler.",
                        TypeEntiteNotification.ACHAT, achat.getIdAchat());
            } else {
                String quand = jours == 0 ? "aujourd'hui" : "dans " + jours + " jour(s)";
                creer(souhaitee, NiveauNotification.ATTENTION, "Échéance d'achat proche",
                        "L'achat n°" + achat.getIdAchat() + " doit être payé avant le " + date + " (" + quand
                                + ") : il reste " + montant + " à régler.",
                        TypeEntiteNotification.ACHAT, achat.getIdAchat());
            }
        }
    }

    private void verifierRefusEnAttente() {
        List<LigneReceptionAchatEntity> enAttente = entityManager.createQuery("""
                        select l from LigneReceptionAchatEntity l
                        where l.receptionAchatEntity.statut = :statut
                          and l.decisionRefus = :decision
                        """, LigneReceptionAchatEntity.class)
                .setParameter("statut", StatutReception.VALIDEE)
                .setParameter("decision", DecisionRefus.EN_ATTENTE)
                .getResultList();

        Set<Long> idsEnAttente = new HashSet<>();
        for (LigneReceptionAchatEntity ligne : enAttente) {
            idsEnAttente.add(ligne.getIdLigneReception());

            if (!notificationRepository.existsByTypeNotificationAndTypeEntiteAndIdEntiteAndStatutNot(
                    TypeNotification.REFUS_EN_ATTENTE, TypeEntiteNotification.LIGNE_RECEPTION_ACHAT,
                    ligne.getIdLigneReception(), StatutNotification.RESOLUE)) {

                creer(TypeNotification.REFUS_EN_ATTENTE, NiveauNotification.ATTENTION,
                        "Refus de marchandise en attente de décision",
                        "Refus en attente de décision : " + ligne.getQuantiteRefusee() + " unité(s) de "
                                + ligne.getLigneAchatEntity().getProduitEntity().getNom()
                                + " (réception n°" + ligne.getReceptionAchatEntity().getIdReceptionAchat()
                                + ", achat n°" + ligne.getReceptionAchatEntity().getAchatEntity().getIdAchat() + ").",
                        TypeEntiteNotification.LIGNE_RECEPTION_ACHAT, ligne.getIdLigneReception());
            }
        }

        // Ferme les alertes dont la décision a été prise.
        for (NotificationEntity notification : notificationRepository.findByTypeNotificationInAndStatutNot(
                List.of(TypeNotification.REFUS_EN_ATTENTE), StatutNotification.RESOLUE)) {
            if (!idsEnAttente.contains(notification.getIdEntite())) {
                resoudre(notification);
            }
        }
    }

    // ------------------------------------------------------------------ Consultation

    @Override
    @Transactional(readOnly = true)
    public List<NotificationResponseDto> lister(StatutNotification statut) {
        List<NotificationEntity> notifications = statut != null
                ? notificationRepository.findByStatutOrderByDateCreationDesc(statut)
                : notificationRepository.findByStatutInOrderByDateCreationDesc(
                List.of(StatutNotification.NON_LUE, StatutNotification.LUE));
        return notifications.stream().map(mapper::toResponseDto).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public long compterNonLues() {
        return notificationRepository.countByStatut(StatutNotification.NON_LUE);
    }

    @Override
    @Transactional
    public NotificationResponseDto marquerCommeLue(Long id) {
        NotificationEntity notification = notificationRepository.findById(id).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Notification introuvable : " + id));
        if (notification.getStatut() == StatutNotification.NON_LUE) {
            notification.setStatut(StatutNotification.LUE);
            notification.setDateLecture(LocalDateTime.now());
            notificationRepository.save(notification);
        }
        return mapper.toResponseDto(notification);
    }

    @Override
    @Transactional
    public void marquerToutesCommeLues() {
        LocalDateTime maintenant = LocalDateTime.now();
        for (NotificationEntity notification :
                notificationRepository.findByStatutOrderByDateCreationDesc(StatutNotification.NON_LUE)) {
            notification.setStatut(StatutNotification.LUE);
            notification.setDateLecture(maintenant);
        }
    }

    // ------------------------------------------------------------------ Outils

    private void creer(TypeNotification type, NiveauNotification niveau, String titre, String message,
                       TypeEntiteNotification typeEntite, Long idEntite) {
        NotificationEntity notification = new NotificationEntity();
        notification.setTypeNotification(type);
        notification.setNiveau(niveau);
        notification.setTitre(titre);
        notification.setMessage(message.length() > 1000 ? message.substring(0, 1000) : message);
        notification.setDateCreation(LocalDateTime.now());
        notification.setStatut(StatutNotification.NON_LUE);
        notification.setTypeEntite(typeEntite);
        notification.setIdEntite(idEntite);
        notificationRepository.save(notification);
    }

    private void resoudre(NotificationEntity notification) {
        notification.setStatut(StatutNotification.RESOLUE);
        notification.setDateResolution(LocalDateTime.now());
        notificationRepository.save(notification);
    }
}