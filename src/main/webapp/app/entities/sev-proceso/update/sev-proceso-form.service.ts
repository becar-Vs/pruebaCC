import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { ISevProceso, NewSevProceso } from '../sev-proceso.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts ISevProceso for edit and NewSevProcesoFormGroupInput for create.
 */
type SevProcesoFormGroupInput = ISevProceso | PartialWithRequiredKeyOf<NewSevProceso>;

type SevProcesoFormDefaults = Pick<NewSevProceso, 'id'>;

type SevProcesoFormGroupContent = {
  id: FormControl<ISevProceso['id'] | NewSevProceso['id']>;
  idProceso: FormControl<ISevProceso['idProceso']>;
  nombre: FormControl<ISevProceso['nombre']>;
  fkIdResponsable: FormControl<ISevProceso['fkIdResponsable']>;
  unidadNegocio: FormControl<ISevProceso['unidadNegocio']>;
};

export type SevProcesoFormGroup = FormGroup<SevProcesoFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class SevProcesoFormService {
  createSevProcesoFormGroup(sevProceso: SevProcesoFormGroupInput = { id: null }): SevProcesoFormGroup {
    const sevProcesoRawValue = {
      ...this.getFormDefaults(),
      ...sevProceso,
    };
    return new FormGroup<SevProcesoFormGroupContent>({
      id: new FormControl(
        { value: sevProcesoRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      idProceso: new FormControl(sevProcesoRawValue.idProceso, {
        validators: [Validators.required],
      }),
      nombre: new FormControl(sevProcesoRawValue.nombre, {
        validators: [Validators.required],
      }),
      fkIdResponsable: new FormControl(sevProcesoRawValue.fkIdResponsable, {
        validators: [Validators.required],
      }),
      unidadNegocio: new FormControl(sevProcesoRawValue.unidadNegocio),
    });
  }

  getSevProceso(form: SevProcesoFormGroup): ISevProceso | NewSevProceso {
    return form.getRawValue() as ISevProceso | NewSevProceso;
  }

  resetForm(form: SevProcesoFormGroup, sevProceso: SevProcesoFormGroupInput): void {
    const sevProcesoRawValue = { ...this.getFormDefaults(), ...sevProceso };
    form.reset(
      {
        ...sevProcesoRawValue,
        id: { value: sevProcesoRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): SevProcesoFormDefaults {
    return {
      id: null,
    };
  }
}
