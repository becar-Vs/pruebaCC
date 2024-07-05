import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { ISevGrafica, NewSevGrafica } from '../sev-grafica.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts ISevGrafica for edit and NewSevGraficaFormGroupInput for create.
 */
type SevGraficaFormGroupInput = ISevGrafica | PartialWithRequiredKeyOf<NewSevGrafica>;

type SevGraficaFormDefaults = Pick<NewSevGrafica, 'id'>;

type SevGraficaFormGroupContent = {
  id: FormControl<ISevGrafica['id'] | NewSevGrafica['id']>;
  idGrafica: FormControl<ISevGrafica['idGrafica']>;
  nombre: FormControl<ISevGrafica['nombre']>;
  fkIdResponsable: FormControl<ISevGrafica['fkIdResponsable']>;
  proceso: FormControl<ISevGrafica['proceso']>;
};

export type SevGraficaFormGroup = FormGroup<SevGraficaFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class SevGraficaFormService {
  createSevGraficaFormGroup(sevGrafica: SevGraficaFormGroupInput = { id: null }): SevGraficaFormGroup {
    const sevGraficaRawValue = {
      ...this.getFormDefaults(),
      ...sevGrafica,
    };
    return new FormGroup<SevGraficaFormGroupContent>({
      id: new FormControl(
        { value: sevGraficaRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      idGrafica: new FormControl(sevGraficaRawValue.idGrafica, {
        validators: [Validators.required],
      }),
      nombre: new FormControl(sevGraficaRawValue.nombre, {
        validators: [Validators.required],
      }),
      fkIdResponsable: new FormControl(sevGraficaRawValue.fkIdResponsable, {
        validators: [Validators.required],
      }),
      proceso: new FormControl(sevGraficaRawValue.proceso),
    });
  }

  getSevGrafica(form: SevGraficaFormGroup): ISevGrafica | NewSevGrafica {
    return form.getRawValue() as ISevGrafica | NewSevGrafica;
  }

  resetForm(form: SevGraficaFormGroup, sevGrafica: SevGraficaFormGroupInput): void {
    const sevGraficaRawValue = { ...this.getFormDefaults(), ...sevGrafica };
    form.reset(
      {
        ...sevGraficaRawValue,
        id: { value: sevGraficaRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): SevGraficaFormDefaults {
    return {
      id: null,
    };
  }
}
