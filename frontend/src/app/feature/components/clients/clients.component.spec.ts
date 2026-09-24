import { Component, computed, inject, signal } from '@angular/core';
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
  private fb = inject(FormBuilder);
  private clientService = inject(ClientService);

  // Liste des clients affichée dans le tableau
  clients = computed(() => this.clientService.clients());

  // Affichage ou non de la modale du formulaire
  showModal = signal(false);

  // null = mode "ajout", un nombre = id du client en cours de modification
  editingId = signal<number | null>(null);

  form = this.fb.group({
    nom: ['', Validators.required],
    email: ['', [Validators.required, Validators.email]],
    telephone: [''],
    entreprise: ['']
  });

  openAddForm() {
    this.editingId.set(null);
    this.form.reset();
    this.showModal.set(true);
  }

  openEditForm(client: clients) {
    this.editingId.set(client.id);
    this.form.setValue({
      nom: client.nom,
      email: client.email,
      telephone: client.telephone,
      entreprise: client.entreprise ?? ''
    });
    this.showModal.set(true);
  }

  closeForm() {
    this.showModal.set(false);
  }

  save() {
    if (this.form.invalid) {
      this.form.markAllAsTouched();
      return;
    }

    const value = this.form.getRawValue();
    const payload = {
      nom: value.nom!,
      email: value.email!,
      telephone: value.telephone ?? '',
      entreprise: value.entreprise ?? ''
    };

    if (this.editingId() !== null) {
      this.clientService.update(this.editingId()!, payload);
    } else {
      this.clientService.add(payload);
    }

    this.closeForm();
  }

  remove(client: clients) {
    if (confirm(`Supprimer le client "${client.nom}" ?`)) {
      this.clientService.delete(client.id);
    }
  }
}
