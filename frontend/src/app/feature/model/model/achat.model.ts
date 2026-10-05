
export interface achat {
  idAchat: number
  dateAchat: string
  total: number
  montantPaye: number
  resteAPayer: number
  typePaiement: string
  dateEcheance: string | null
  statut: string
  idFournisseur: number
  nomFournisseur: string
}
