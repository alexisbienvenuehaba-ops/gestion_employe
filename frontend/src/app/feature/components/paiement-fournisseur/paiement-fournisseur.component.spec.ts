import { ComponentFixture, TestBed } from '@angular/core/testing';
import { PaiementFournisseurComponent } from './paiement-fournisseur.component';

describe('PaiementFournisseurComponent', () => {
  let component: PaiementFournisseurComponent;
  let fixture: ComponentFixture<PaiementFournisseurComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [PaiementFournisseurComponent]
    })
      .compileComponents();

    fixture = TestBed.createComponent(PaiementFournisseurComponent);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
