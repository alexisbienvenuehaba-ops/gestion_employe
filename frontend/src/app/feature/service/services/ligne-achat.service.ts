import { Injectable, signal } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { ligneAchat } from '../../model/model/ligne-achat.model';

@Injectable({
  providedIn: 'root'
})
export class LigneAchatService {

  private readonly apiUrl = 'http://localhost:8080/api/ligne-achat';

  readonly ligneAchats = signal<ligneAchat[]>([]);

  constructor(private http: HttpClient) {}

  getAll(): void {
    this.http.get<ligneAchat[]>(this.apiUrl)
      .subscribe({
        next: (data) => {
          this.ligneAchats.set(data);
        },
        error: (error) => {
          console.error('Erreur chargement lignes achat :', error);
        }
      });
  }

  getByAchat(idAchat: number): void {
    this.http.get<ligneAchat[]>(
      `${this.apiUrl}/achat/${idAchat}`
    ).subscribe({
      next: (data) => {
        this.ligneAchats.set(data);
      },
      error: (error) => {
        console.error('Erreur chargement lignes de l’achat :', error);
      }
    });
  }

  add(ligne: {
    quantite: number;
    prixUnitaire: number;
    idAchat: number;
    idProduit: number;
  }) {
    return this.http.post<ligneAchat>(
      this.apiUrl,
      ligne
    );
  }

  update(
    id: number,
    ligne: {
      quantite: number;
      prixUnitaire: number;
      idAchat: number;
      idProduit: number;
    }
  ) {
    return this.http.put<ligneAchat>(
      `${this.apiUrl}/${id}`,
      ligne
    );
  }

  delete(id: number) {
    return this.http.delete(
      `${this.apiUrl}/${id}`
    );
  }
}
