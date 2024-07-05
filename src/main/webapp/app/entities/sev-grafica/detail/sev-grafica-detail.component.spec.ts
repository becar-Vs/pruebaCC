import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness } from '@angular/router/testing';
import { of } from 'rxjs';

import { SevGraficaDetailComponent } from './sev-grafica-detail.component';

describe('SevGrafica Management Detail Component', () => {
  let comp: SevGraficaDetailComponent;
  let fixture: ComponentFixture<SevGraficaDetailComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [SevGraficaDetailComponent],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              component: SevGraficaDetailComponent,
              resolve: { sevGrafica: () => of({ id: 123 }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    })
      .overrideTemplate(SevGraficaDetailComponent, '')
      .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(SevGraficaDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load sevGrafica on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', SevGraficaDetailComponent);

      // THEN
      expect(instance.sevGrafica()).toEqual(expect.objectContaining({ id: 123 }));
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
