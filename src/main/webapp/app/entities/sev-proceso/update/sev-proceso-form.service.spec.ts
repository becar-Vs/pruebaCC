import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../sev-proceso.test-samples';

import { SevProcesoFormService } from './sev-proceso-form.service';

describe('SevProceso Form Service', () => {
  let service: SevProcesoFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(SevProcesoFormService);
  });

  describe('Service methods', () => {
    describe('createSevProcesoFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createSevProcesoFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            idProceso: expect.any(Object),
            nombre: expect.any(Object),
            fkIdResponsable: expect.any(Object),
            unidadNegocio: expect.any(Object),
          }),
        );
      });

      it('passing ISevProceso should create a new form with FormGroup', () => {
        const formGroup = service.createSevProcesoFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            idProceso: expect.any(Object),
            nombre: expect.any(Object),
            fkIdResponsable: expect.any(Object),
            unidadNegocio: expect.any(Object),
          }),
        );
      });
    });

    describe('getSevProceso', () => {
      it('should return NewSevProceso for default SevProceso initial value', () => {
        const formGroup = service.createSevProcesoFormGroup(sampleWithNewData);

        const sevProceso = service.getSevProceso(formGroup) as any;

        expect(sevProceso).toMatchObject(sampleWithNewData);
      });

      it('should return NewSevProceso for empty SevProceso initial value', () => {
        const formGroup = service.createSevProcesoFormGroup();

        const sevProceso = service.getSevProceso(formGroup) as any;

        expect(sevProceso).toMatchObject({});
      });

      it('should return ISevProceso', () => {
        const formGroup = service.createSevProcesoFormGroup(sampleWithRequiredData);

        const sevProceso = service.getSevProceso(formGroup) as any;

        expect(sevProceso).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing ISevProceso should not enable id FormControl', () => {
        const formGroup = service.createSevProcesoFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewSevProceso should disable id FormControl', () => {
        const formGroup = service.createSevProcesoFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
