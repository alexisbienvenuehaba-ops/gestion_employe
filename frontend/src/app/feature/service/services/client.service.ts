import { Injectable, inject, signal } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { clients } from '../../model/model/client.model';

const API_URL = 'http://localhost:8080/api/clients';

@Injectable({ providedIn: 'root' })
export class ClientService {
  private http = inject(HttpClient);

  // Liste des clients, désormais chargée depuis le backend
  readonly clients = signal<clients[]>([]);

  constructor() {
    this.loadAll();
  }

  loadAll(): void {
    this.http.get<clients[]>(API_URL).subscribe({
      next: (data) => this.clients.set(data),
      error: (err) => console.error('Erreur lors du chargement des clients', err)
    });
  }

  add(client: Omit<clients, 'id'>): void {
    this.http.post<clients>(API_URL, client).subscribe({
      next: (created) => this.clients.update(list => [...list, created]),
      error: (err) => console.error('Erreur lors de l\'ajout du client', err)
    });
  }

  update(id: number, changes: Omit<clients, 'id'>): void {
    this.http.put<clients>(`${API_URL}/${id}`, changes).subscribe({
      next: (updated) => this.clients.update(list =>
        list.map(c => c.id === id ? updated : c)
      ),
      error: (err) => console.error('Erreur lors de la modification du client', err)
    });
  }

  delete(id: number): void {
    this.http.delete<void>(`${API_URL}/${id}`).subscribe({
      next: () => this.clients.update(list => list.filter(c => c.id !== id)),
      error: (err) => console.error('Erreur lors de la suppression du client', err)
    });
  }
}
