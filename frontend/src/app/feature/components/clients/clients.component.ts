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

import { clients } from '../../model/model/client.model';
import { ClientService } from '../../service/services/client.service';

@Component({
  selector: 'app-clients',
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
  templateUrl: './clients.component.html',
  styleUrl: './clients.component.scss'
})
export class ClientsComponent {
  private readonly fb = new FormBuilder();
  private readonly clientService: ClientService;

  readonly clients = computed(() => this.clientService.clients());
  readonly showModal = signal(false);
  readonly editingId = signal<number | null>(null);

  readonly form = this.fb.group({
    nom: ['', Validators.required],
    prenom:['',Validators.required],
    email: ['', [Validators.required, Validators.email]],
    telephone: [''],
    entreprise: ['']
  });

  constructor(clientService: ClientService) {
    this.clientService = clientService;
  }

  openAddForm(): void {
    this.editingId.set(null);
    this.form.reset();
    this.showModal.set(true);
  }

  openEditForm(client: clients): void {
    this.editingId.set(client.id);
    this.form.setValue({
      nom: client.nom,
      prenom: client.prenom,
      email: client.email,
      telephone: client.telephone,
      entreprise: client.entreprise ?? ''
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
      entreprise: value.entreprise ?? ''
    };

    const id = this.editingId();
    if (id !== null) {
      this.clientService.update(id, payload);
    } else {
      this.clientService.add(payload);
    }

    this.closeForm();
  }

  remove(client: clients): void {
    const confirmed = confirm(`Supprimer le client "${client.nom}" ?`);
    if (confirmed) {
      this.clientService.delete(client.id);
    }
  }
}
