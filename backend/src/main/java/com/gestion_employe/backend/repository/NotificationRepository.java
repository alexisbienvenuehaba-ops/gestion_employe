package com.gestion_employe.backend.repository;

import com.gestion_employe.backend.Enum.StatutNotification;
import com.gestion_employe.backend.Enum.TypeEntiteNotification;
import com.gestion_employe.backend.Enum.TypeNotification;
import com.gestion_employe.backend.entity.NotificationEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;

public interface NotificationRepository extends JpaRepository<NotificationEntity, Long> {

    List<NotificationEntity> findByStatutOrderByDateCreationDesc(StatutNotification statut);

    List<NotificationEntity> findByStatutInOrderByDateCreationDesc(Collection<StatutNotification> statuts);

    long countByStatut(StatutNotification statut);

    /** Notifications d'un type donné qui ne sont pas encore fermées. */
    List<NotificationEntity> findByTypeNotificationInAndStatutNot(Collection<TypeNotification> types,
                                                                  StatutNotification statut);

    List<NotificationEntity> findByTypeEntiteAndIdEntiteAndStatutNot(TypeEntiteNotification typeEntite,
                                                                     Long idEntite,
                                                                     StatutNotification statut);

    boolean existsByTypeNotificationAndTypeEntiteAndIdEntiteAndStatutNot(TypeNotification typeNotification,
                                                                         TypeEntiteNotification typeEntite,
                                                                         Long idEntite,
                                                                         StatutNotification statut);
}