import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { ISevUnidadNegocio, NewSevUnidadNegocio } from '../sev-unidad-negocio.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts ISevUnidadNegocio for edit and NewSevUnidadNegocioFormGroupInput for create.
 */
type SevUnidadNegocioFormGroupInput = ISevUnidadNegocio | PartialWithRequiredKeyOf<NewSevUnidadNegocio>;

type SevUnidadNegocioFormDefaults = Pick<NewSevUnidadNegocio, 'id'>;

type SevUnidadNegocioFormGroupContent = {
  id: FormControl<ISevUnidadNegocio['id'] | NewSevUnidadNegocio['id']>;
  idUnidadNegocio: FormControl<ISevUnidadNegocio['idUnidadNegocio']>;
  nombre: FormControl<ISevUnidadNegocio['nombre']>;
  fkIdResponsable: FormControl<ISevUnidadNegocio['fkIdResponsable']>;
};

export type SevUnidadNegocioFormGroup = FormGroup<SevUnidadNegocioFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class SevUnidadNegocioFormService {
  createSevUnidadNegocioFormGroup(sevUnidadNegocio: SevUnidadNegocioFormGroupInput = { id: null }): SevUnidadNegocioFormGroup {
    const sevUnidadNegocioRawValue = {
      ...this.getFormDefaults(),
      ...sevUnidadNegocio,
    };
    return new FormGroup<SevUnidadNegocioFormGroupContent>({
      id: new FormControl(
        { value: sevUnidadNegocioRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      idUnidadNegocio: new FormControl(sevUnidadNegocioRawValue.idUnidadNegocio, {
        validators: [Validators.required],
      }),
      nombre: new FormControl(sevUnidadNegocioRawValue.nombre, {
        validators: [Validators.required],
      }),
      fkIdResponsable: new FormControl(sevUnidadNegocioRawValue.fkIdResponsable, {
        validators: [Validators.required],
      }),
    });
  }

  getSevUnidadNegocio(form: SevUnidadNegocioFormGroup): ISevUnidadNegocio | NewSevUnidadNegocio {
    return form.getRawValue() as ISevUnidadNegocio | NewSevUnidadNegocio;
  }

  resetForm(form: SevUnidadNegocioFormGroup, sevUnidadNegocio: SevUnidadNegocioFormGroupInput): void {
    const sevUnidadNegocioRawValue = { ...this.getFormDefaults(), ...sevUnidadNegocio };
    form.reset(
      {
        ...sevUnidadNegocioRawValue,
        id: { value: sevUnidadNegocioRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): SevUnidadNegocioFormDefaults {
    return {
      id: null,
    };
  }
}
