import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../sev-grafica.test-samples';

import { SevGraficaFormService } from './sev-grafica-form.service';

describe('SevGrafica Form Service', () => {
  let service: SevGraficaFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(SevGraficaFormService);
  });

  describe('Service methods', () => {
    describe('createSevGraficaFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createSevGraficaFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            idGrafica: expect.any(Object),
            nombre: expect.any(Object),
            fkIdResponsable: expect.any(Object),
            proceso: expect.any(Object),
          }),
        );
      });

      it('passing ISevGrafica should create a new form with FormGroup', () => {
        const formGroup = service.createSevGraficaFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            idGrafica: expect.any(Object),
            nombre: expect.any(Object),
            fkIdResponsable: expect.any(Object),
            proceso: expect.any(Object),
          }),
        );
      });
    });

    describe('getSevGrafica', () => {
      it('should return NewSevGrafica for default SevGrafica initial value', () => {
        const formGroup = service.createSevGraficaFormGroup(sampleWithNewData);

        const sevGrafica = service.getSevGrafica(formGroup) as any;

        expect(sevGrafica).toMatchObject(sampleWithNewData);
      });

      it('should return NewSevGrafica for empty SevGrafica initial value', () => {
        const formGroup = service.createSevGraficaFormGroup();

        const sevGrafica = service.getSevGrafica(formGroup) as any;

        expect(sevGrafica).toMatchObject({});
      });

      it('should return ISevGrafica', () => {
        const formGroup = service.createSevGraficaFormGroup(sampleWithRequiredData);

        const sevGrafica = service.getSevGrafica(formGroup) as any;

        expect(sevGrafica).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing ISevGrafica should not enable id FormControl', () => {
        const formGroup = service.createSevGraficaFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewSevGrafica should disable id FormControl', () => {
        const formGroup = service.createSevGraficaFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
