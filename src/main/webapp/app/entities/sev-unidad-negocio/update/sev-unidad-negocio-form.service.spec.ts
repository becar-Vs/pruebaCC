import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../sev-unidad-negocio.test-samples';

import { SevUnidadNegocioFormService } from './sev-unidad-negocio-form.service';

describe('SevUnidadNegocio Form Service', () => {
  let service: SevUnidadNegocioFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(SevUnidadNegocioFormService);
  });

  describe('Service methods', () => {
    describe('createSevUnidadNegocioFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createSevUnidadNegocioFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            idUnidadNegocio: expect.any(Object),
            nombre: expect.any(Object),
            fkIdResponsable: expect.any(Object),
          }),
        );
      });

      it('passing ISevUnidadNegocio should create a new form with FormGroup', () => {
        const formGroup = service.createSevUnidadNegocioFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            idUnidadNegocio: expect.any(Object),
            nombre: expect.any(Object),
            fkIdResponsable: expect.any(Object),
          }),
        );
      });
    });

    describe('getSevUnidadNegocio', () => {
      it('should return NewSevUnidadNegocio for default SevUnidadNegocio initial value', () => {
        const formGroup = service.createSevUnidadNegocioFormGroup(sampleWithNewData);

        const sevUnidadNegocio = service.getSevUnidadNegocio(formGroup) as any;

        expect(sevUnidadNegocio).toMatchObject(sampleWithNewData);
      });

      it('should return NewSevUnidadNegocio for empty SevUnidadNegocio initial value', () => {
        const formGroup = service.createSevUnidadNegocioFormGroup();

        const sevUnidadNegocio = service.getSevUnidadNegocio(formGroup) as any;

        expect(sevUnidadNegocio).toMatchObject({});
      });

      it('should return ISevUnidadNegocio', () => {
        const formGroup = service.createSevUnidadNegocioFormGroup(sampleWithRequiredData);

        const sevUnidadNegocio = service.getSevUnidadNegocio(formGroup) as any;

        expect(sevUnidadNegocio).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing ISevUnidadNegocio should not enable id FormControl', () => {
        const formGroup = service.createSevUnidadNegocioFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewSevUnidadNegocio should disable id FormControl', () => {
        const formGroup = service.createSevUnidadNegocioFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
