import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../sev-grafica-data.test-samples';

import { SevGraficaDataFormService } from './sev-grafica-data-form.service';

describe('SevGraficaData Form Service', () => {
  let service: SevGraficaDataFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(SevGraficaDataFormService);
  });

  describe('Service methods', () => {
    describe('createSevGraficaDataFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createSevGraficaDataFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            idRow: expect.any(Object),
            fechaObjetivo: expect.any(Object),
            valorObjetivo: expect.any(Object),
            valorLogrado: expect.any(Object),
            grafica: expect.any(Object),
          }),
        );
      });

      it('passing ISevGraficaData should create a new form with FormGroup', () => {
        const formGroup = service.createSevGraficaDataFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            idRow: expect.any(Object),
            fechaObjetivo: expect.any(Object),
            valorObjetivo: expect.any(Object),
            valorLogrado: expect.any(Object),
            grafica: expect.any(Object),
          }),
        );
      });
    });

    describe('getSevGraficaData', () => {
      it('should return NewSevGraficaData for default SevGraficaData initial value', () => {
        const formGroup = service.createSevGraficaDataFormGroup(sampleWithNewData);

        const sevGraficaData = service.getSevGraficaData(formGroup) as any;

        expect(sevGraficaData).toMatchObject(sampleWithNewData);
      });

      it('should return NewSevGraficaData for empty SevGraficaData initial value', () => {
        const formGroup = service.createSevGraficaDataFormGroup();

        const sevGraficaData = service.getSevGraficaData(formGroup) as any;

        expect(sevGraficaData).toMatchObject({});
      });

      it('should return ISevGraficaData', () => {
        const formGroup = service.createSevGraficaDataFormGroup(sampleWithRequiredData);

        const sevGraficaData = service.getSevGraficaData(formGroup) as any;

        expect(sevGraficaData).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing ISevGraficaData should not enable id FormControl', () => {
        const formGroup = service.createSevGraficaDataFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewSevGraficaData should disable id FormControl', () => {
        const formGroup = service.createSevGraficaDataFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
