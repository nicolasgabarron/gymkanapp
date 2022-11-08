import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IEquipo, NewEquipo } from '../equipo.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IEquipo for edit and NewEquipoFormGroupInput for create.
 */
type EquipoFormGroupInput = IEquipo | PartialWithRequiredKeyOf<NewEquipo>;

type EquipoFormDefaults = Pick<NewEquipo, 'id'>;

type EquipoFormGroupContent = {
  id: FormControl<IEquipo['id'] | NewEquipo['id']>;
  identificador: FormControl<IEquipo['identificador']>;
  nombre: FormControl<IEquipo['nombre']>;
  cantidadIntegrantes: FormControl<IEquipo['cantidadIntegrantes']>;
};

export type EquipoFormGroup = FormGroup<EquipoFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class EquipoFormService {
  createEquipoFormGroup(equipo: EquipoFormGroupInput = { id: null }): EquipoFormGroup {
    const equipoRawValue = {
      ...this.getFormDefaults(),
      ...equipo,
    };
    return new FormGroup<EquipoFormGroupContent>({
      id: new FormControl(
        { value: equipoRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      identificador: new FormControl(equipoRawValue.identificador),
      nombre: new FormControl(equipoRawValue.nombre),
      cantidadIntegrantes: new FormControl(equipoRawValue.cantidadIntegrantes, {
        validators: [Validators.required, Validators.min(1)],
      }),
    });
  }

  getEquipo(form: EquipoFormGroup): IEquipo | NewEquipo {
    return form.getRawValue() as IEquipo | NewEquipo;
  }

  resetForm(form: EquipoFormGroup, equipo: EquipoFormGroupInput): void {
    const equipoRawValue = { ...this.getFormDefaults(), ...equipo };
    form.reset(
      {
        ...equipoRawValue,
        id: { value: equipoRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): EquipoFormDefaults {
    return {
      id: null,
    };
  }
}
