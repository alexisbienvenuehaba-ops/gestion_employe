import { Injectable, inject, signal } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { tap } from 'rxjs/operators';

export interface PaiementFournisseur {
  idPaiementFournisseur: number;
  datePaiement: string;
  montant: number;
  modePaiement: string;
  reference: string | null;
  statut: string;
  observation: string | null;
  dateValidation: string | null;
  dateAnnulation: string | null;
  motifAnnulation: string | null;
  idAchat: number;
  idResponsable: number | null;
  idResponsableAnnulation: number | null;
}

export interface PaiementFournisseurRequest {
  idAchat: number;
  montant: number;
  modePaiement: string;
  datePaiement?: string;
  reference?: string;
  observation?: string;
}

export interface SoldeAchat {
  total: number;
  montantPaye: number;
  resteAPayer: number;
  statutPaiement: string;
  dateEcheance: string | null;
}

@Injectable({
  providedIn: 'root'
})
export class PaiementFournisseurService {

  private readonly http = inject(HttpClient);

  private readonly apiUrl =
    'http://localhost:8080/api/paiements-fournisseur';

  readonly paiements = signal<PaiementFournisseur[]>([]);

  getAll(): void {
    this.http.get<PaiementFournisseur[]>(this.apiUrl).subscribe({
      next: (data) => {
        this.paiements.set(data);
      },
      error: (error) => {
        console.error('Erreur chargement paiements fournisseur :', error);
      }
    });
  }

  getById(id: number) {
    return this.http.get<PaiementFournisseur>(
      `${this.apiUrl}/${id}`
    );
  }

  getByAchat(idAchat: number) {
    return this.http.get<PaiementFournisseur[]>(
      `${this.apiUrl}/achat/${idAchat}`
    );
  }

  getSoldeAchat(idAchat: number) {
    return this.http.get<SoldeAchat>(
      `${this.apiUrl}/achat/${idAchat}/solde`
    );
  }

  creer(data: PaiementFournisseurRequest) {
    return this.http.post<PaiementFournisseur>(
      this.apiUrl,
      data
    ).pipe(
      tap((nouveauPaiement) => {
        this.paiements.update(paiements => [
          ...paiements,
          nouveauPaiement
        ]);
      })
    );
  }

  valider(id: number, data: any) {
    return this.http.put<PaiementFournisseur>(
      `${this.apiUrl}/${id}/valider`,
      data
    ).pipe(
      tap((paiementValide) => {
        this.paiements.update(paiements =>
          paiements.map(p =>
            p.idPaiementFournisseur === id
              ? paiementValide
              : p
          )
        );
      })
    );
  }

  annuler(id: number, data: any) {
    return this.http.put<PaiementFournisseur>(
      `${this.apiUrl}/${id}/annuler`,
      data
    ).pipe(
      tap((paiementAnnule) => {
        this.paiements.update(paiements =>
          paiements.map(p =>
            p.idPaiementFournisseur === id
              ? paiementAnnule
              : p
          )
        );
      })
    );
  }
}
