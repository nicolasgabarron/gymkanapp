import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IPuntoControl, NewPuntoControl } from '../punto-control.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IPuntoControl for edit and NewPuntoControlFormGroupInput for create.
 */
type PuntoControlFormGroupInput = IPuntoControl | PartialWithRequiredKeyOf<NewPuntoControl>;

type PuntoControlFormDefaults = Pick<NewPuntoControl, 'id'>;

type PuntoControlFormGroupContent = {
  id: FormControl<IPuntoControl['id'] | NewPuntoControl['id']>;
  identificador: FormControl<IPuntoControl['identificador']>;
  orden: FormControl<IPuntoControl['orden']>;
  nombre: FormControl<IPuntoControl['nombre']>;
  direccion: FormControl<IPuntoControl['direccion']>;
};

export type PuntoControlFormGroup = FormGroup<PuntoControlFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class PuntoControlFormService {
  createPuntoControlFormGroup(puntoControl: PuntoControlFormGroupInput = { id: null }): PuntoControlFormGroup {
    const puntoControlRawValue = {
      ...this.getFormDefaults(),
      ...puntoControl,
    };
    return new FormGroup<PuntoControlFormGroupContent>({
      id: new FormControl(
        { value: puntoControlRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      identificador: new FormControl(puntoControlRawValue.identificador),
      orden: new FormControl(puntoControlRawValue.orden),
      nombre: new FormControl(puntoControlRawValue.nombre),
      direccion: new FormControl(puntoControlRawValue.direccion),
    });
  }

  getPuntoControl(form: PuntoControlFormGroup): IPuntoControl | NewPuntoControl {
    return form.getRawValue() as IPuntoControl | NewPuntoControl;
  }

  resetForm(form: PuntoControlFormGroup, puntoControl: PuntoControlFormGroupInput): void {
    const puntoControlRawValue = { ...this.getFormDefaults(), ...puntoControl };
    form.reset(
      {
        ...puntoControlRawValue,
        id: { value: puntoControlRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): PuntoControlFormDefaults {
    return {
      id: null,
    };
  }
}
