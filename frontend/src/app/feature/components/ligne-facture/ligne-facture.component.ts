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

import { LigneFactureService } from '../../service/services/ligne-facture.service';
import { FactureService } from '../../service/services/facture.service';
import { ProduitService } from '../../service/services/produit.service';

import { ligneFacture } from '../../model/model/ligneFactureModel';

@Component({
  selector: 'app-ligne-facture',
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
  templateUrl: './ligne-facture.component.html',
  styleUrl: './ligne-facture.component.scss'
})
export class LigneFactureComponent {

  private readonly fb = new FormBuilder();

  private readonly ligneFactureService: LigneFactureService;
  private readonly factureService: FactureService;
  private readonly produitService: ProduitService;

  readonly ligneFactures = computed(() =>
    this.ligneFactureService.ligneFactures()
  );

  readonly factures = computed(() =>
    this.factureService.facture()
  );

  readonly produits = computed(() =>
    this.produitService.produits()
  );

  readonly showModal = signal(false);

  readonly editingId = signal<number | null>(null);

  readonly form = this.fb.group({

    quantite: [
      null as number | null,
      [
        Validators.required,
        Validators.min(1)
      ]
    ],

    prixUnitaire: [
      null as number | null,
      [
        Validators.required,
        Validators.min(0)
      ]
    ],

    idFacture: [
      null as number | null,
      Validators.required
    ],

    idProduit: [
      null as number | null,
      Validators.required
    ]

  });

  constructor(
    ligneFactureService: LigneFactureService,
    factureService: FactureService,
    produitService: ProduitService
  ) {
    this.ligneFactureService = ligneFactureService;
    this.factureService = factureService;
    this.produitService = produitService;
  }

  openAddForm(): void {
    this.editingId.set(null);
    this.form.reset();
    this.showModal.set(true);
  }

  openEditForm(ligne: ligneFacture): void {

    this.editingId.set(ligne.idLigneFacture);

    this.form.setValue({
      quantite: ligne.quantite,
      prixUnitaire: ligne.prixUnitaire,
      idFacture: ligne.idFacture,
      idProduit: ligne.idProduit
    });

    this.showModal.set(true);
  }

  closeForm(): void {
    this.showModal.set(false);
  }

  save(): void {

    if (this.form.invalid) {
      this.form.markAllAsTouched();
      return;
    }

    const value = this.form.getRawValue();

    const payload = {
      idFacture: value.idFacture!,
      idProduit: value.idProduit!,
      quantite: value.quantite!,
      prixUnitaire: value.prixUnitaire!
    };

    const id = this.editingId();

    if (id !== null) {

      this.ligneFactureService.update(
        id,
        payload
      );

    } else {

      this.ligneFactureService.add(
        payload
      );

    }

    this.closeForm();
  }

  remove(ligne: ligneFacture): void {

    const confirmed = confirm(
      'Supprimer cette ligne de facture ?'
    );

    if (confirmed) {

      this.ligneFactureService.delete(
        ligne.idLigneFacture
      );

    }
  }
}
