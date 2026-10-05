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

import { achat } from '../../model/model/achat.model';

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
    ],

    montantPaye: [
      0,
      [
        Validators.required,
        Validators.min(0)
      ]
    ]
  });

  constructor(
    achatService: AchatService,
    fournisseurService: FournisseurService,
    produitService: ProduitService
  ) {
    this.achatService = achatService;
    this.fournisseurService = fournisseurService;
    this.produitService = produitService;
  }

  openAddForm(): void {

    this.editingId.set(null);

    this.form.reset({
      dateAchat: '',
      idFournisseur: null,
      typePaiement: '',
      dateEcheance: null,
      montantPaye: 0
    });

    this.lignes.set([]);

    this.showModal.set(true);
  }

  openEditForm(achat: achat): void {

    this.editingId.set(achat.idAchat);

    this.form.patchValue({
      dateAchat: achat.dateAchat,
      idFournisseur: achat.idFournisseur,
      typePaiement: achat.typePaiement,
      dateEcheance: achat.dateEcheance,
      montantPaye: achat.montantPaye
    });

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

  getResteAPayer(): number {

    const montantPaye =
      Number(
        this.form.get('montantPaye')?.value
      ) || 0;

    return Math.max(
      this.getTotal() - montantPaye,
      0
    );
  }

  save(): void {

    if (this.form.invalid) {

      this.form.markAllAsTouched();

      return;
    }

    if (this.lignes().length === 0) {

      alert(
        'Ajoutez au moins un produit à l’achat.'
      );

      return;
    }

    const value =
      this.form.getRawValue();

    const payload = {

      dateAchat: value.dateAchat!,

      idFournisseur:
        value.idFournisseur!,

      typePaiement:
        value.typePaiement!,

      dateEcheance:
        value.typePaiement === 'A_TERME'
          ? value.dateEcheance
          : null,

      montantPaye:
        Number(value.montantPaye) || 0
    };

    const id = this.editingId();

    if (id !== null) {

      this.achatService.update(
        id,
        payload
      );

    } else {

      this.achatService.add(
        payload
      );
    }

    this.closeForm();
  }

  remove(achat: achat): void {

    const confirmed = confirm(
      'Supprimer cet achat ?'
    );

    if (confirmed) {

      this.achatService.delete(
        achat.idAchat
      );
    }
  }
}

