import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness } from '@angular/router/testing';
import { of } from 'rxjs';

import { SevProcesoDetailComponent } from './sev-proceso-detail.component';

describe('SevProceso Management Detail Component', () => {
  let comp: SevProcesoDetailComponent;
  let fixture: ComponentFixture<SevProcesoDetailComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [SevProcesoDetailComponent],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              component: SevProcesoDetailComponent,
              resolve: { sevProceso: () => of({ id: 123 }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    })
      .overrideTemplate(SevProcesoDetailComponent, '')
      .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(SevProcesoDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load sevProceso on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', SevProcesoDetailComponent);

      // THEN
      expect(instance.sevProceso()).toEqual(expect.objectContaining({ id: 123 }));
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
