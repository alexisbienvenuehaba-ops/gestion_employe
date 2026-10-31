import { Injectable, computed, inject, signal } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { NotificationModel } from '../../model/model/notification.model';

const API_URL = 'http://localhost:8080/api/notifications';
const DELAI_ACTUALISATION_MS = 60_000;

@Injectable({ providedIn: 'root' })
export class NotificationService {

  private http = inject(HttpClient);

  readonly notifications = signal<NotificationModel[]>([]);
  readonly nonLues = computed(() => this.notifications().filter(n => n.statut === 'NON_LUE').length);

  constructor() {
    this.verifierEtCharger();
    setInterval(() => this.loadAll(), DELAI_ACTUALISATION_MS);
  }

  /** Demande au backend de vérifier les échéances, puis charge la liste. */
  verifierEtCharger(): void {
    this.http.post<void>(`${API_URL}/verifier`, {}).subscribe({
      next: () => this.loadAll(),
      error: () => this.loadAll()
    });
  }

  loadAll(): void {
    this.http.get<NotificationModel[]>(API_URL).subscribe({
      next: (data) => this.notifications.set(data),
      error: (err) => console.error('Erreur lors du chargement des notifications', err)
    });
  }

  marquerCommeLue(id: number): void {
    this.http.put<NotificationModel>(`${API_URL}/${id}/lue`, {}).subscribe({
      next: (updated) => this.notifications.update(list =>
        list.map(n => n.idNotification === id ? updated : n)
      ),
      error: (err) => console.error('Erreur lors du marquage de la notification', err)
    });
  }

  marquerToutesCommeLues(): void {
    this.http.put<void>(`${API_URL}/lues`, {}).subscribe({
      next: () => this.notifications.update(list =>
        list.map(n => n.statut === 'NON_LUE' ? { ...n, statut: 'LUE' as const } : n)
      ),
      error: (err) => console.error('Erreur lors du marquage des notifications', err)
    });
  }
}
