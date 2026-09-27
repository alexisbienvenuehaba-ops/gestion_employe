import { Injectable, inject, signal } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { produit } from '../../model/model/produit.model';

const API_URL = 'http://localhost:8080/api/produit';

@Injectable({ providedIn: 'root' })
export class ProduitService {

  private http = inject(HttpClient);

  readonly produits = signal<produit[]>([]);

  constructor() {
    this.loadAll();
  }

  loadAll(): void {
    this.http.get<produit[]>(API_URL).subscribe({
      next: (data) => this.produits.set(data),
      error: (err) => console.error('Erreur lors du chargement des produits', err)
    });
  }

  add(produit: Omit<produit, 'idProduit' | 'nomFournisseur'>): void {
    this.http.post<produit>(API_URL, produit).subscribe({
      next: (created) => this.produits.update(list => [...list, created]),
      error: (err) => console.error('Erreur lors de l\'ajout du produit', err)
    });
  }

  update(id: number, changes: Omit<produit, 'idProduit' | 'nomFournisseur'>): void {
    this.http.put<produit>(`${API_URL}/${id}`, changes).subscribe({
      next: (updated) => this.produits.update(list =>
        list.map(p => p.idProduit === id ? updated : p)
      ),
      error: (err) => console.error('Erreur lors de la modification du produit', err)
    });
  }

  delete(id: number): void {
    this.http.delete<void>(`${API_URL}/${id}`).subscribe({
      next: () => this.produits.update(list => list.filter(p => p.idProduit !== id)),
      error: (err) => console.error('Erreur lors de la suppression du produit', err)
    });
  }
}
