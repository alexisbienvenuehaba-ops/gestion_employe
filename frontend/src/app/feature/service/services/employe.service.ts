import { Injectable, inject, signal } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { employes } from '../../model/model/employe.model';

const API_URL = 'http://localhost:8080/api/employe';

@Injectable({ providedIn: 'root' })
export class EmployeService {

  private http = inject(HttpClient);

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

  add(employe: Omit<employes, 'idEmploye'>): void {
    this.http.post<employes>(API_URL, employe).subscribe({
      next: (created) => this.employes.update(list => [...list, created]),
      error: (err) => console.error('Erreur lors de l\'ajout de l\'employé', err)
    });
  }

  update(id: number, changes: Omit<employes, 'idEmploye'>): void {
    this.http.put<employes>(`${API_URL}/${id}`, changes).subscribe({
      next: (updated) => this.employes.update(list =>
        list.map(e => e.idEmploye === id ? updated : e)
      ),
      error: (err) => console.error('Erreur lors de la modification de l\'employé', err)
    });
  }

  delete(id: number): void {
    this.http.delete<void>(`${API_URL}/${id}`).subscribe({
      next: () => this.employes.update(list => list.filter(e => e.idEmploye !== id)),
      error: (err) => console.error('Erreur lors de la suppression de l\'employé', err)
    });
  }
}
