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
  ModalBodyComponent,
  ModalComponent,
  ModalFooterComponent,
  ModalHeaderComponent,
  ModalTitleDirective,
  RowComponent,
  TableDirective
} from '@coreui/angular';
import { IconDirective } from '@coreui/icons-angular';


import {FournisseurService} from '../../service/services/fournisseur.service';
import {fournisseur} from '../../model/model/fournisseur.model';

@Component({
  selector: 'app-fournisseur',
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
    TableDirective,
    IconDirective
  ],
  templateUrl: 'fournisseur.component.html',
  styleUrl: './fournisseur.component.scss'
})
export class FournisseurComponent {

  private readonly fb = new FormBuilder();
  private readonly fournisseurService: FournisseurService;

  readonly fournisseur = computed(() => this.fournisseurService.fournisseur());
  readonly showModal = signal(false);
  readonly editingId = signal<number | null>(null);

  readonly form = this.fb.group({
    nom: ['', Validators.required],
    prenom: ['', Validators.required],
    telephone: [''],
  });

  constructor(fournisseurService: FournisseurService) {
    this.fournisseurService = fournisseurService;
  }

  openAddForm(): void {
    this.editingId.set(null);
    this.form.reset();
    this.showModal.set(true);
  }

  openEditForm(fournisseur: fournisseur): void {
    this.editingId.set(fournisseur.idFournisseur);
    this.form.setValue({
      nom: fournisseur.nom,
      prenom: fournisseur.prenom,
      telephone: fournisseur.telephone,
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
      prenom: value.prenom!,
      telephone: value.telephone ?? '',
    };

    const id = this.editingId();
    if (id !== null) {
      this.fournisseurService.update(id, payload);
    } else {
      this.fournisseurService.add(payload);
    }

    this.closeForm();
  }

  remove(fournisseur: fournisseur): void {
    const confirmed = confirm(`Supprimer le fournisseur "${fournisseur.nom}" ?`);
    if (confirmed) {
      this.fournisseurService.delete(fournisseur.idFournisseur);
    }
  }


}
