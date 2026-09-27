import { Component, computed, signal } from '@angular/core';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import {
  ButtonDirective,
  CardBodyComponent,
  CardComponent,
  CardHeaderComponent,
  ColComponent,
  FormControlDirective,
  FormLabelDirective,
  FormSelectDirective,
  ModalBodyComponent,
  ModalComponent,
  ModalFooterComponent,
  ModalHeaderComponent,
  ModalTitleDirective,
  RowComponent,
  TableDirective
} from '@coreui/angular';
import { IconDirective } from '@coreui/icons-angular';

import { ProduitService } from '../../service/services/produit.service';
import { produit } from '../../model/model/produit.model';
import { FournisseurService } from '../../service/services/fournisseur.service';

@Component({
  selector: 'app-produit',
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
    FormSelectDirective,
    ModalBodyComponent,
    ModalComponent,
    ModalFooterComponent,
    ModalHeaderComponent,
    ModalTitleDirective,
    RowComponent,
    TableDirective,
    IconDirective
  ],
  templateUrl: 'produit.component.html',
  styleUrl: './produit.component.scss'
})
export class ProduitComponent {

  private readonly fb = new FormBuilder();
  private readonly produitService: ProduitService;
  private readonly fournisseurService: FournisseurService;

  readonly produits = computed(() => this.produitService.produits());
  readonly fournisseurs = computed(() => this.fournisseurService.fournisseur());

  readonly showModal = signal(false);
  readonly editingId = signal<number | null>(null);

  readonly form = this.fb.group({
    nom: ['', Validators.required],
    quantiteStock: [0, [Validators.required, Validators.min(0)]],
    prix: [0, [Validators.required, Validators.min(0.01)]],
    idFournisseur: [null as number | null, Validators.required],
  });

  constructor(produitService: ProduitService, fournisseurService: FournisseurService) {
    this.produitService = produitService;
    this.fournisseurService = fournisseurService;
  }

  openAddForm(): void {
    this.editingId.set(null);
    this.form.reset({ nom: '', quantiteStock: 0, prix: 0, idFournisseur: null });
    this.showModal.set(true);
  }

  openEditForm(produit: produit): void {
    this.editingId.set(produit.idProduit);
    this.form.setValue({
      nom: produit.nom,
      quantiteStock: produit.quantiteStock,
      prix: produit.prix,
      idFournisseur: produit.idFournisseur,
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
      nom: value.nom!,
      quantiteStock: value.quantiteStock!,
      prix: value.prix!,
      idFournisseur: value.idFournisseur!,
    };

    const id = this.editingId();
    if (id !== null) {
      this.produitService.update(id, payload);
    } else {
      this.produitService.add(payload);
    }

    this.closeForm();
  }

  remove(produit: produit): void {
    const confirmed = confirm(`Supprimer le produit "${produit.nom}" ?`);
    if (confirmed) {
      this.produitService.delete(produit.idProduit);
    }
  }
}
