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


import { EmployeService } from '../../service/services/employe.service';
import {employes} from '../../model/model/employe.model';

@Component({
  selector: 'app-employe',
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
  templateUrl: 'employe.component.html',
  styleUrl:'./employe.component.scss'
})
export class EmployeComponent {

  private readonly fb = new FormBuilder();
  private readonly employeService : EmployeService;

  readonly employes = computed(() => this.employeService.employes());
  readonly showModal = signal(false);
  readonly editingId = signal<number | null>(null);

  readonly form = this.fb.group({
    nom: ['', Validators.required],
    prenom:['',Validators.required],
    email: ['', [Validators.required, Validators.email]],
    telephone: [''],
    poste: ['']
  });

  constructor(employeService: EmployeService) {
    this.employeService = employeService;
  }

  openAddForm(): void {
    this.editingId.set(null);
    this.form.reset();
    this.showModal.set(true);
  }
  openEditForm(employes: employes): void {
    this.editingId.set(employes.id);
    this.form.setValue({
      nom: employes.nom,
      prenom: employes.prenom,
      email: employes.email,
      telephone: employes.telephone,
      poste: employes.poste ?? ''
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
      email: value.email!,
      telephone: value.telephone ?? '',
      poste: value.poste ?? ''
    };

    const id = this.editingId();
    if (id !== null) {
      this.employeService.update(id, payload);
    } else {
      this.employeService.add(payload);
    }

    this.closeForm();
  }

  remove(employes: employes): void {
    const confirmed = confirm(`Supprimer le l\'employe "${employes.nom}" ?`);
    if (confirmed) {
      this.employeService.delete(employes.id);
    }
  }

}
