import { DecimalPipe } from '@angular/common';
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

import { PaiementFournisseurService } from '../../service/services/paiement-fournisseur.service';
import { AchatService } from '../../service/services/achat.service';
import { EmployeService } from '../../service/services/employe.service';

import { PaiementFournisseur } from '../../model/model/paiement-fournisseur.model';

@Component({
  selector: 'app-paiements-fournisseur',
  standalone: true,
  imports: [
    ReactiveFormsModule,
    DecimalPipe,

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
  templateUrl: './paiement-fournisseur.component.html',
  styleUrl: './paiement-fournisseur.component.scss'
})
export class PaiementsFournisseurComponent {

  private readonly fb = new FormBuilder();

  private readonly paiementService: PaiementFournisseurService;
  private readonly achatService: AchatService;
  private readonly employeService: EmployeService;

  readonly paiements = computed(() =>
    this.paiementService.paiements()
  );

  readonly achats = computed(() =>
    this.achatService.achats()
  );

  readonly employes = computed(() =>
    this.employeService.employes()
  );

  readonly showModal = signal(false);

  readonly showValidationModal = signal(false);

  readonly paiementAValider =
    signal<PaiementFournisseur | null>(null);

  readonly editingId =
    signal<number | null>(null);

  readonly form = this.fb.group({

    idAchat: [
      null as number | null,
      Validators.required
    ],

    montant: [
      0,
      [
        Validators.required,
        Validators.min(1)
      ]
    ],

    modePaiement: [
      '',
      Validators.required
    ],

    datePaiement: [
      '',
      Validators.required
    ],

    reference: [
      ''
    ],

    observation: [
      ''
    ]
  });

  readonly formValidation = this.fb.group({

    idResponsable: [
      null as number | null,
      Validators.required
    ]

  });

  constructor(
    paiementService: PaiementFournisseurService,
    achatService: AchatService,
    employeService: EmployeService
  ) {

    this.paiementService = paiementService;
    this.achatService = achatService;
    this.employeService = employeService;

    this.paiementService.getAll();
    this.achatService.getAll();
    this.employeService.loadAll();
  }


  openAddForm(): void {

    this.editingId.set(null);

    this.form.reset({

      idAchat: null,
      montant: 0,
      modePaiement: '',
      datePaiement: '',
      reference: '',
      observation: ''

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

      idAchat: value.idAchat!,

      montant: Number(value.montant),

      modePaiement: value.modePaiement!,

      datePaiement: value.datePaiement!,

      reference:
        value.reference || undefined,

      observation:
        value.observation || undefined

    };

    this.paiementService.creer(payload).subscribe({

      next: () => {

        this.paiementService.getAll();
        this.achatService.getAll();

        this.closeForm();
      },

      error: (error) => {

        console.error(
          'Erreur création paiement fournisseur :',
          error
        );

        alert(
          error?.error?.message ??
          'Erreur lors de la création du paiement.'
        );
      }

    });
  }


  valider(paiement: PaiementFournisseur): void {

    if (paiement.statut !== 'BROUILLON') {
      return;
    }

    if (this.employes().length === 0) {

      alert(
        'Aucun employé disponible pour valider le paiement.'
      );

      return;
    }

    this.paiementAValider.set(paiement);

    this.formValidation.reset({
      idResponsable: null
    });

    this.showValidationModal.set(true);
  }


  confirmerValidation(): void {

    if (this.formValidation.invalid) {

      this.formValidation.markAllAsTouched();

      return;
    }

    const paiement = this.paiementAValider();

    if (!paiement) {
      return;
    }

    const idResponsable =
      this.formValidation.getRawValue().idResponsable;

    if (!idResponsable) {
      return;
    }

    this.paiementService.valider(

      paiement.idPaiementFournisseur,

      {
        idResponsable: idResponsable
      }

    ).subscribe({

      next: () => {

        this.paiementService.getAll();
        this.achatService.getAll();

        this.showValidationModal.set(false);

        this.paiementAValider.set(null);
      },

      error: (error) => {

        console.error(
          'Erreur validation paiement :',
          error
        );

        alert(
          error?.error?.message ??
          'Erreur lors de la validation du paiement.'
        );
      }

    });
  }


  fermerValidation(): void {

    this.showValidationModal.set(false);

    this.paiementAValider.set(null);
  }


  annuler(paiement: PaiementFournisseur): void {

    const motif = prompt(
      'Motif de l’annulation du paiement :'
    );

    if (!motif || !motif.trim()) {
      return;
    }

    this.paiementService.annuler(

      paiement.idPaiementFournisseur,

      {
        motifAnnulation: motif.trim()
      }

    ).subscribe({

      next: () => {

        this.paiementService.getAll();
        this.achatService.getAll();

      },

      error: (error) => {

        console.error(
          'Erreur annulation paiement :',
          error
        );

        alert(
          error?.error?.message ??
          'Erreur lors de l’annulation du paiement.'
        );
      }

    });
  }
}
