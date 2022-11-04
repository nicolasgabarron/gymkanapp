import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IVoluntario, NewVoluntario } from '../voluntario.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IVoluntario for edit and NewVoluntarioFormGroupInput for create.
 */
type VoluntarioFormGroupInput = IVoluntario | PartialWithRequiredKeyOf<NewVoluntario>;

type VoluntarioFormDefaults = Pick<NewVoluntario, 'id'>;

type VoluntarioFormGroupContent = {
  id: FormControl<IVoluntario['id'] | NewVoluntario['id']>;
  dni: FormControl<IVoluntario['dni']>;
  nombre: FormControl<IVoluntario['nombre']>;
  apellidos: FormControl<IVoluntario['apellidos']>;
  fechaNacimiento: FormControl<IVoluntario['fechaNacimiento']>;
  usuarioApp: FormControl<IVoluntario['usuarioApp']>;
  puntoControl: FormControl<IVoluntario['puntoControl']>;
};

export type VoluntarioFormGroup = FormGroup<VoluntarioFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class VoluntarioFormService {
  createVoluntarioFormGroup(voluntario: VoluntarioFormGroupInput = { id: null }): VoluntarioFormGroup {
    const voluntarioRawValue = {
      ...this.getFormDefaults(),
      ...voluntario,
    };
    return new FormGroup<VoluntarioFormGroupContent>({
      id: new FormControl(
        { value: voluntarioRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      dni: new FormControl(voluntarioRawValue.dni),
      nombre: new FormControl(voluntarioRawValue.nombre),
      apellidos: new FormControl(voluntarioRawValue.apellidos),
      fechaNacimiento: new FormControl(voluntarioRawValue.fechaNacimiento),
      usuarioApp: new FormControl(voluntarioRawValue.usuarioApp),
      puntoControl: new FormControl(voluntarioRawValue.puntoControl),
    });
  }

  getVoluntario(form: VoluntarioFormGroup): IVoluntario | NewVoluntario {
    return form.getRawValue() as IVoluntario | NewVoluntario;
  }

  resetForm(form: VoluntarioFormGroup, voluntario: VoluntarioFormGroupInput): void {
    const voluntarioRawValue = { ...this.getFormDefaults(), ...voluntario };
    form.reset(
      {
        ...voluntarioRawValue,
        id: { value: voluntarioRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): VoluntarioFormDefaults {
    return {
      id: null,
    };
  }
}
