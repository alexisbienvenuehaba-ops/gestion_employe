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
