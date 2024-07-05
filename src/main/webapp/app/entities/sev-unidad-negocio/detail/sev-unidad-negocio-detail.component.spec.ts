import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness } from '@angular/router/testing';
import { of } from 'rxjs';

import { SevUnidadNegocioDetailComponent } from './sev-unidad-negocio-detail.component';

describe('SevUnidadNegocio Management Detail Component', () => {
  let comp: SevUnidadNegocioDetailComponent;
  let fixture: ComponentFixture<SevUnidadNegocioDetailComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [SevUnidadNegocioDetailComponent],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              component: SevUnidadNegocioDetailComponent,
              resolve: { sevUnidadNegocio: () => of({ id: 123 }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    })
      .overrideTemplate(SevUnidadNegocioDetailComponent, '')
      .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(SevUnidadNegocioDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load sevUnidadNegocio on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', SevUnidadNegocioDetailComponent);

      // THEN
      expect(instance.sevUnidadNegocio()).toEqual(expect.objectContaining({ id: 123 }));
    });
  });

  describe('PreviousState', () => {
    it('Should navigate to previous state', () => {
      jest.spyOn(window.history, 'back');
      comp.previousState();
      expect(window.history.back).toHaveBeenCalled();
    });
  });
});
