import { Component, computed, signal } from '@angular/core';
import {
  FormBuilder,
  ReactiveFormsModule,
  Validators
} from '@angular/forms';

import {
  ButtonDirective,
  CardBodyComponent,
  CardComponent,
  CardHeaderComponent,
  ColComponent,
  FormControlDirective,
  FormLabelDirective,
  ModalBodyComponent,
  ModalComponent,
  ModalFooterComponent,
  ModalHeaderComponent,
  ModalTitleDirective,
  RowComponent,
  TableDirective
} from '@coreui/angular';

import { AchatService } from '../../service/services/achat.service';
import { FournisseurService } from '../../service/services/fournisseur.service';
import { ProduitService } from '../../service/services/produit.service';
import { LigneAchatService } from '../../service/services/ligne-achat.service';

import { IconDirective } from '@coreui/icons-angular';
import { achat } from '../../model/model/achat.model';

import { from } from 'rxjs';
import { concatMap } from 'rxjs/operators';

@Component({
  selector: 'app-achat',
  standalone: true,
  imports: [
    ReactiveFormsModule,
    ButtonDirective,
    CardBodyComponent,
    CardComponent,
    CardHeaderComponent,
    ColComponent,
    FormControlDirective,
    FormLabelDirective,
    IconDirective,
    ModalBodyComponent,
    ModalComponent,
    ModalFooterComponent,
    ModalHeaderComponent,
    ModalTitleDirective,
    RowComponent,
    TableDirective
  ],
  templateUrl: './achat.component.html',
  styleUrl: './achat.component.scss'
})
export class AchatComponent {

  private readonly fb = new FormBuilder();

  private readonly achatService: AchatService;
  private readonly fournisseurService: FournisseurService;
  private readonly produitService: ProduitService;
  private readonly ligneAchatService: LigneAchatService;

  readonly achats = computed(() =>
    this.achatService.achats()
  );

  readonly fournisseurs = computed(() =>
    this.fournisseurService.fournisseur()
  );

  readonly produits = computed(() =>
    this.produitService.produits()
  );

  readonly showModal = signal(false);

  readonly editingId = signal<number | null>(null);

  readonly lignes = signal<any[]>([]);

  /**
   * Vrai quand il y a au moins une ligne et que chaque ligne
   * a un produit, une quantité et un prix supérieurs à 0.
   */
  readonly lignesValides = computed(() =>
    this.lignes().length > 0 &&
    this.lignes().every(ligne =>
      Number(ligne.idProduit) > 0 &&
      Number(ligne.quantite) > 0 &&
      Number(ligne.prixUnitaire) > 0
    )
  );

  /**
   * Passe à vrai quand on clique sur Enregistrer :
   * fait apparaître en rouge les champs manquants.
   */
  readonly tentativeEnregistrement = signal(false);

  readonly form = this.fb.group({
    dateAchat: [
      '',
      Validators.required
    ],

    idFournisseur: [
      null as number | null,
      Validators.required
    ],

    typePaiement: [
      '',
      Validators.required
    ],

    dateEcheance: [
      null as string | null
    ]
  });

  constructor(
    achatService: AchatService,
    fournisseurService: FournisseurService,
    produitService: ProduitService,
    ligneAchatService: LigneAchatService
  ) {
    this.achatService = achatService;
    this.fournisseurService = fournisseurService;
    this.produitService = produitService;
    this.ligneAchatService = ligneAchatService;

    this.achatService.getAll();
  }

  openAddForm(): void {
    this.tentativeEnregistrement.set(false);

    this.editingId.set(null);

    this.form.reset({
      dateAchat: '',
      idFournisseur: null,
      typePaiement: '',
      dateEcheance: null
    });

    this.lignes.set([]);

    this.showModal.set(true);
  }

  openEditForm(achat: achat): void {
    this.tentativeEnregistrement.set(false);

    this.editingId.set(achat.idAchat);

    this.form.patchValue({
      dateAchat: achat.dateAchat,
      idFournisseur: achat.idFournisseur,
      typePaiement: achat.typePaiement,
      dateEcheance: achat.dateEcheance
    });

    // Les lignes ne sont pas rechargées à la modification :
    // on vide celles d'un formulaire précédent.
    this.lignes.set([]);

    this.showModal.set(true);
  }

  closeForm(): void {
    this.showModal.set(false);
  }

  addLigne(): void {
    this.lignes.update(lignes => [
      ...lignes,
      {
        idProduit: null,
        quantite: 1,
        prixUnitaire: 0
      }
    ]);
  }

  removeLigne(index: number): void {
    this.lignes.update(lignes =>
      lignes.filter((_, i) => i !== index)
    );
  }

  updateLigne(
    index: number,
    field: string,
    value: any
  ): void {
    this.lignes.update(lignes => {
      const updated = [...lignes];

      updated[index] = {
        ...updated[index],
        [field]: Number(value)
      };

      return updated;
    });
  }

  getTotal(): number {
    return this.lignes().reduce(
      (total, ligne) =>
        total +
        (
          Number(ligne.quantite) *
          Number(ligne.prixUnitaire)
        ),
      0
    );
  }

  save(): void {

    this.tentativeEnregistrement.set(true);

    if (this.form.invalid) {
      this.form.markAllAsTouched();
      return;
    }

    const value = this.form.getRawValue();

    if (value.typePaiement === 'A_TERME' && !value.dateEcheance) {
      alert(
        'La date d’échéance est obligatoire pour un paiement à terme.'
      );
      return;
    }

    const payload = {
      dateAchat: value.dateAchat!,
      idFournisseur: value.idFournisseur!,
      typePaiement: value.typePaiement!,
      dateEcheance:
        value.typePaiement === 'A_TERME'
          ? value.dateEcheance
          : null
    };

    const id = this.editingId();

    // =========================
    // MODIFICATION
    // =========================
    if (id !== null) {

      this.achatService.update(
        id,
        payload
      ).subscribe({
        next: () => {

          this.achatService.getAll();
          this.closeForm();

        },

        error: (error) => {

          console.error(
            'Erreur modification achat :',
            error
          );

          alert(
            'Erreur lors de la modification de l’achat.'
          );
        }
      });

      return;
    }

    // =========================
    // CRÉATION : les lignes sont vérifiées AVANT de créer l'achat
    // =========================
    if (!this.lignesValides()) {
      alert(
        'Ajoutez au moins un produit, et chaque ligne doit avoir un produit, une quantité et un prix supérieurs à 0.'
      );
      return;
    }

    // =========================
    // AJOUT DE L'ACHAT
    // =========================
    this.achatService.add(
      payload
    ).subscribe({

      next: (newAchat) => {

        console.log(
          'ACHAT CRÉÉ :',
          newAchat
        );

        // =========================
        // AJOUT DES LIGNES
        // =========================
        from(this.lignes())
          .pipe(
            concatMap(ligne =>
              this.ligneAchatService.add({
                quantite: Number(ligne.quantite),
                prixUnitaire: Number(ligne.prixUnitaire),
                idAchat: newAchat.idAchat,
                idProduit: Number(ligne.idProduit)
              })
            )
          )
          .subscribe({

            next: (ligne) => {
              console.log(
                'LIGNE ACHAT CRÉÉE :',
                ligne
              );
            },

            complete: () => {

              console.log(
                'Toutes les lignes ont été enregistrées.'
              );

              this.achatService.getAll();
              this.closeForm();

            },

            error: (error) => {

              console.error(
                'Erreur création ligne achat :',
                error
              );

              alert(
                'L’achat a été créé, mais une erreur est survenue lors de l’enregistrement des lignes.'
              );

              this.achatService.getAll();
            }

          });
      },

      error: (error) => {

        console.error(
          'Erreur création achat :',
          error
        );

        alert(
          'Erreur lors de la création de l’achat.'
        );
      }

    });
  }

  remove(achat: achat): void {

    const confirmed =
      confirm(
        'Supprimer cet achat ?'
      );

    if (confirmed) {

      this.achatService.delete(
        achat.idAchat
      );

    }
  }
}
