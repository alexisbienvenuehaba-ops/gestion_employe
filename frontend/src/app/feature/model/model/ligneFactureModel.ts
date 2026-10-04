export interface ligneFacture {
  idLigneFacture: number;
  quantite: number;
  prixUnitaire: number;

  idFacture: number;
  numeroFacture: string;
  nomClient: string;

  idProduit: number;
  nomProduit: string;
}
