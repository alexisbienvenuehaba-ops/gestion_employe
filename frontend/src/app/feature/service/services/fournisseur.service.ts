import { Injectable, inject, signal } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { fournisseur } from '../../model/model/fournisseur.model'
const API_URL = 'http://localhost:8080/api/fournisseur';

@Injectable({ providedIn: 'root' })
export class FournisseurService {

  private http = inject(HttpClient);

  readonly fournisseur = signal<fournisseur[]>([]);

  constructor() {
    this.loadAll();
  }

  loadAll(): void {
    this.http.get<fournisseur[]>(API_URL).subscribe({
      next: (data) => this.fournisseur.set(data),
      error: (err) => console.error('Erreur lors du chargement des fournisseurs', err)
    });
  }

  add(fournisseur: Omit<fournisseur, 'idFournisseur'>): void {
    this.http.post<fournisseur>(API_URL, fournisseur).subscribe({
      next: (created) => this.fournisseur.update(list => [...list, created]),
      error: (err) => console.error('Erreur lors de l\'ajout du fournisseur', err)
    });
  }

  update(id: number, changes: Omit<fournisseur, 'idFournisseur'>): void {
    this.http.put<fournisseur>(`${API_URL}/${id}`, changes).subscribe({
      next: (updated) => this.fournisseur.update(list =>
        list.map(e => e.idFournisseur === id ? updated : e)
      ),
      error: (err) => console.error('Erreur lors de la modification du fournisseur', err)
    });
  }

  delete(id: number): void {
    this.http.delete<void>(`${API_URL}/${id}`).subscribe({
      next: () => this.fournisseur.update(list => list.filter(e => e.idFournisseur !== id)),
      error: (err) => console.error('Erreur lors de la suppression du fournisseur', err)
    });
  }
}
