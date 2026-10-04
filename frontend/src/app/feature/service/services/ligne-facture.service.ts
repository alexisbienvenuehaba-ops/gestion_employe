import { Injectable, inject, signal } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { ligneFacture } from '../../model/model/ligneFactureModel';

const API_URL = 'http://localhost:8080/api/ligne_facture';

@Injectable({
  providedIn: 'root'
})
export class LigneFactureService {

  private http = inject(HttpClient);

  readonly ligneFactures = signal<ligneFacture[]>([]);

  constructor() {
    this.loadAll();
  }

  loadAll(): void {
    this.http.get<ligneFacture[]>(API_URL).subscribe({
      next: (data) => this.ligneFactures.set(data),
      error: (err) =>
        console.error(
          'Erreur lors du chargement des lignes de facture',
          err
        )
    });
  }

  add(data: {
    idFacture: number;
    idProduit: number;
    quantite: number;
    prixUnitaire: number;
  }): void {

    this.http.post<ligneFacture>(API_URL, data).subscribe({
      next: (created) => {
        this.ligneFactures.update(list => [
          ...list,
          created
        ]);
      },

      error: (err) =>
        console.error(
          'Erreur lors de l\'ajout de la ligne de facture',
          err
        )
    });
  }

  update(
    id: number,
    data: {
      idFacture: number;
      idProduit: number;
      quantite: number;
      prixUnitaire: number;
    }
  ): void {

    this.http
      .put<ligneFacture>(`${API_URL}/${id}`, data)
      .subscribe({
        next: (updated) => {

          this.ligneFactures.update(list =>
            list.map(ligne =>
              ligne.idLigneFacture === id
                ? updated
                : ligne
            )
          );

        },

        error: (err) =>
          console.error(
            'Erreur lors de la modification de la ligne de facture',
            err
          )
      });
  }

  delete(id: number): void {

    this.http
      .delete<void>(`${API_URL}/${id}`)
      .subscribe({

        next: () => {

          this.ligneFactures.update(list =>
            list.filter(
              ligne => ligne.idLigneFacture !== id
            )
          );

        },

        error: (err) =>
          console.error(
            'Erreur lors de la suppression de la ligne de facture',
            err
          )
      });
  }
}
