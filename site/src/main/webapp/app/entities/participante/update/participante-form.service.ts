import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IParticipante, NewParticipante } from '../participante.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IParticipante for edit and NewParticipanteFormGroupInput for create.
 */
type ParticipanteFormGroupInput = IParticipante | PartialWithRequiredKeyOf<NewParticipante>;

type ParticipanteFormDefaults = Pick<NewParticipante, 'id'>;

type ParticipanteFormGroupContent = {
  id: FormControl<IParticipante['id'] | NewParticipante['id']>;
  dni: FormControl<IParticipante['dni']>;
  nombre: FormControl<IParticipante['nombre']>;
  apellidos: FormControl<IParticipante['apellidos']>;
  fechaNacimiento: FormControl<IParticipante['fechaNacimiento']>;
  usuarioApp: FormControl<IParticipante['usuarioApp']>;
  equipo: FormControl<IParticipante['equipo']>;
};

export type ParticipanteFormGroup = FormGroup<ParticipanteFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class ParticipanteFormService {
  createParticipanteFormGroup(participante: ParticipanteFormGroupInput = { id: null }): ParticipanteFormGroup {
    const participanteRawValue = {
      ...this.getFormDefaults(),
      ...participante,
    };
    return new FormGroup<ParticipanteFormGroupContent>({
      id: new FormControl(
        { value: participanteRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      dni: new FormControl(participanteRawValue.dni),
      nombre: new FormControl(participanteRawValue.nombre),
      apellidos: new FormControl(participanteRawValue.apellidos),
      fechaNacimiento: new FormControl(participanteRawValue.fechaNacimiento),
      usuarioApp: new FormControl(participanteRawValue.usuarioApp),
      equipo: new FormControl(participanteRawValue.equipo),
    });
  }

  getParticipante(form: ParticipanteFormGroup): IParticipante | NewParticipante {
    return form.getRawValue() as IParticipante | NewParticipante;
  }

  resetForm(form: ParticipanteFormGroup, participante: ParticipanteFormGroupInput): void {
    const participanteRawValue = { ...this.getFormDefaults(), ...participante };
    form.reset(
      {
        ...participanteRawValue,
        id: { value: participanteRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): ParticipanteFormDefaults {
    return {
      id: null,
    };
  }
}
