import { Service } from '@angular/core';
import { Injectable, inject, signal } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { employes } from '../../model/model/employe.model'

const API_URL = 'http://localhost:8080/api/employe';

@Injectable({ providedIn: 'root' })
export class EmployeService {

  private http = inject(HttpClient);

  // Liste des employés, désormais chargée depuis le backend
  readonly employes = signal<employes[]>([]);

  constructor() {
    this.loadAll();
  }

  loadAll(): void {
    this.http.get<employes[]>(API_URL).subscribe({
      next: (data) => this.employes.set(data),
      error: (err) => console.error('Erreur lors du chargement des employés', err)
    });
  }

  add(employes: Omit<employes, 'id'>): void {
    this.http.post<employes>(API_URL, employes).subscribe({
      next: (created) => this.employes.update(list => [...list, created]),
      error: (err) => console.error('Erreur lors de l\'ajout de l', err)
    });
  }
  update(id: number, changes: Omit<employes, 'id'>): void {
    this.http.put<employes>(`${API_URL}/${id}`, changes).subscribe({
      next: (updated) => this.employes.update(list =>
        list.map(c => c.id === id ? updated : c)
      ),
      error: (err) => console.error('Erreur lors de la modification de l\'employe', err)
    });
  }
  delete(id: number): void {
    this.http.delete<void>(`${API_URL}/${id}`).subscribe({
      next: () => this.employes.update(list => list.filter(c => c.id !== id)),
      error: (err) => console.error('Erreur lors de la suppression de l\'employe', err)
    });
  }

}
