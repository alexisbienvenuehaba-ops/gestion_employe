import { Injectable, inject, signal } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { facture } from '../../model/model/facture.model';

const API_URL = 'http://localhost:8080/api/facture';

@Injectable({ providedIn: 'root' })
export class FactureService {

  private http = inject(HttpClient);

  readonly facture = signal<facture[]>([]);

  constructor() {
    this.loadAll();
  }

  loadAll(): void {
    this.http.get<facture[]>(API_URL).subscribe({
      next: (data) => this.facture.set(data),
      error: (err) => console.error('Erreur lors du chargement des factures', err)
    });
  }

  add(data: Omit<facture, 'idFacture' | 'numeroFacture' | 'nomEmploye' | 'nomClient'>): void {
    this.http.post<facture>(API_URL, {
      statut: data.statut,
      dateFacture: data.dateFacture,
      idEmploye: data.idEmploye,
      id: data.id
    }).subscribe({
      next: (created) => this.facture.update(list => [...list, created]),
      error: (err) => console.error('Erreur lors de l\'ajout de la facture', err)
    });
  }

  update(
    id: number,
    changes: Omit<facture, 'idFacture' | 'numeroFacture' | 'nomEmploye' | 'nomClient'>
  ): void {
    this.http.put<facture>(`${API_URL}/${id}`, changes).subscribe({
      next: (updated) => this.facture.update(list =>
        list.map(f => f.idFacture === id ? updated : f)
      ),
      error: (err) => console.error('Erreur lors de la modification de la facture', err)
    });
  }

  delete(id: number): void {
    this.http.delete<void>(`${API_URL}/${id}`).subscribe({
      next: () => this.facture.update(list =>
        list.filter(f => f.idFacture !== id)
      ),
      error: (err) => console.error('Erreur lors de la suppression de la facture', err)
    });
  }
}
