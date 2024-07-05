import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness } from '@angular/router/testing';
import { of } from 'rxjs';

import { SevGraficaDataDetailComponent } from './sev-grafica-data-detail.component';

describe('SevGraficaData Management Detail Component', () => {
  let comp: SevGraficaDataDetailComponent;
  let fixture: ComponentFixture<SevGraficaDataDetailComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [SevGraficaDataDetailComponent],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              component: SevGraficaDataDetailComponent,
              resolve: { sevGraficaData: () => of({ id: 123 }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    })
      .overrideTemplate(SevGraficaDataDetailComponent, '')
      .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(SevGraficaDataDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load sevGraficaData on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', SevGraficaDataDetailComponent);

      // THEN
      expect(instance.sevGraficaData()).toEqual(expect.objectContaining({ id: 123 }));
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
