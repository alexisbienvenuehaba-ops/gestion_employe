package com.gestion_employe.backend.controler;

import com.gestion_employe.backend.Enum.StatutNotification;
import com.gestion_employe.backend.dto.Response.NotificationResponseDto;
import com.gestion_employe.backend.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService notificationService;

    /** Sans paramètre : notifications actives (non lues et lues). */
    @GetMapping
    public List<NotificationResponseDto> lister(@RequestParam(required = false) StatutNotification statut) {
        return notificationService.lister(statut);
    }

    /** Nombre de notifications non lues (pastille de la cloche). */
    @GetMapping("/non-lues/count")
    public long compterNonLues() {
        return notificationService.compterNonLues();
    }

    @PutMapping("/{id}/lue")
    public NotificationResponseDto marquerCommeLue(@PathVariable Long id) {
        return notificationService.marquerCommeLue(id);
    }

    @PutMapping("/lues")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void marquerToutesCommeLues() {
        notificationService.marquerToutesCommeLues();
    }

    /** Lance la vérification tout de suite (sans attendre 8 h). */
    @PostMapping("/verifier")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void verifier() {
        notificationService.verifier();
    }
}