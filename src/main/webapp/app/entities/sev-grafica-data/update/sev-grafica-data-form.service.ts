import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { ISevGraficaData, NewSevGraficaData } from '../sev-grafica-data.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts ISevGraficaData for edit and NewSevGraficaDataFormGroupInput for create.
 */
type SevGraficaDataFormGroupInput = ISevGraficaData | PartialWithRequiredKeyOf<NewSevGraficaData>;

type SevGraficaDataFormDefaults = Pick<NewSevGraficaData, 'id'>;

type SevGraficaDataFormGroupContent = {
  id: FormControl<ISevGraficaData['id'] | NewSevGraficaData['id']>;
  idRow: FormControl<ISevGraficaData['idRow']>;
  fechaObjetivo: FormControl<ISevGraficaData['fechaObjetivo']>;
  valorObjetivo: FormControl<ISevGraficaData['valorObjetivo']>;
  valorLogrado: FormControl<ISevGraficaData['valorLogrado']>;
  grafica: FormControl<ISevGraficaData['grafica']>;
};

export type SevGraficaDataFormGroup = FormGroup<SevGraficaDataFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class SevGraficaDataFormService {
  createSevGraficaDataFormGroup(sevGraficaData: SevGraficaDataFormGroupInput = { id: null }): SevGraficaDataFormGroup {
    const sevGraficaDataRawValue = {
      ...this.getFormDefaults(),
      ...sevGraficaData,
    };
    return new FormGroup<SevGraficaDataFormGroupContent>({
      id: new FormControl(
        { value: sevGraficaDataRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      idRow: new FormControl(sevGraficaDataRawValue.idRow, {
        validators: [Validators.required],
      }),
      fechaObjetivo: new FormControl(sevGraficaDataRawValue.fechaObjetivo, {
        validators: [Validators.required],
      }),
      valorObjetivo: new FormControl(sevGraficaDataRawValue.valorObjetivo, {
        validators: [Validators.required],
      }),
      valorLogrado: new FormControl(sevGraficaDataRawValue.valorLogrado, {
        validators: [Validators.required],
      }),
      grafica: new FormControl(sevGraficaDataRawValue.grafica),
    });
  }

  getSevGraficaData(form: SevGraficaDataFormGroup): ISevGraficaData | NewSevGraficaData {
    return form.getRawValue() as ISevGraficaData | NewSevGraficaData;
  }

  resetForm(form: SevGraficaDataFormGroup, sevGraficaData: SevGraficaDataFormGroupInput): void {
    const sevGraficaDataRawValue = { ...this.getFormDefaults(), ...sevGraficaData };
    form.reset(
      {
        ...sevGraficaDataRawValue,
        id: { value: sevGraficaDataRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): SevGraficaDataFormDefaults {
    return {
      id: null,
    };
  }
}
