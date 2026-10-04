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

import { IconDirective } from '@coreui/icons-angular';

import { FactureService } from '../../service/services/facture.service';
import { ClientService } from '../../service/services/client.service';
import { EmployeService } from '../../service/services/employe.service';

import { facture } from '../../model/model/facture.model';

@Component({
  selector: 'app-facture',
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
  templateUrl: './facture.component.html',
  styleUrl: './facture.component.scss'
})
export class FactureComponent {

  private readonly fb = new FormBuilder();

  private readonly factureService: FactureService;
  private readonly clientService: ClientService;
  private readonly employeService: EmployeService;

  readonly factures = computed(() =>
    this.factureService.facture()
  );

  readonly clients = computed(() =>
    this.clientService.clients()
  );

  readonly employes = computed(() =>
    this.employeService.employes()
  );

  readonly showModal = signal(false);

  readonly editingId = signal<number | null>(null);

  readonly form = this.fb.group({


statut: [
  '',
  Validators.required
],

dateFacture: [
  '',
  Validators.required
],

idEmploye: [
  null as number | null,
  Validators.required
],

id: [
  null as number | null,
  Validators.required
]


});

constructor(
  factureService: FactureService,
  clientService: ClientService,
  employeService: EmployeService
) {
  this.factureService = factureService;
  this.clientService = clientService;
  this.employeService = employeService;
}

openAddForm(): void {


this.editingId.set(null);

this.form.reset();

this.showModal.set(true);


}

openEditForm(facture: facture): void {

this.editingId.set(facture.idFacture);

this.form.setValue({

  statut: facture.statut,

  dateFacture: facture.dateFacture,

  idEmploye: facture.idEmploye,

  id: facture.id

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

  statut: value.statut!,

  dateFacture: value.dateFacture!,

  idEmploye: value.idEmploye!,

  id: value.id!

};

const id = this.editingId();

if (id !== null) {

  this.factureService.update(
    id,
    payload
  );

} else {

  this.factureService.add(
    payload
  );
}

this.closeForm();

}

remove(facture: facture): void {

const confirmed = confirm(
  'Supprimer cette facture ?'
);

if (confirmed) {

  this.factureService.delete(
    facture.idFacture
  );
}

}
}
