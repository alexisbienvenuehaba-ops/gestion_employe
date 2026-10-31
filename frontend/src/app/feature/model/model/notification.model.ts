export type TypeNotification = 'ECHEANCE_ACHAT_PROCHE' | 'ECHEANCE_ACHAT_DEPASSEE' | 'REFUS_EN_ATTENTE';
export type NiveauNotification = 'INFORMATION' | 'ATTENTION' | 'URGENT';
export type StatutNotification = 'NON_LUE' | 'LUE' | 'RESOLUE';
export type TypeEntiteNotification = 'ACHAT' | 'LIGNE_RECEPTION_ACHAT';

export interface NotificationModel {
  idNotification: number;
  typeNotification: TypeNotification;
  niveau: NiveauNotification;
  titre: string;
  message: string;
  dateCreation: string;
  statut: StatutNotification;
  dateLecture: string | null;
  dateResolution: string | null;
  typeEntite: TypeEntiteNotification;
  idEntite: number;
}
