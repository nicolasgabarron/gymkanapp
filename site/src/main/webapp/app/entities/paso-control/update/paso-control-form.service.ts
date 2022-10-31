import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IPasoControl, NewPasoControl } from '../paso-control.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IPasoControl for edit and NewPasoControlFormGroupInput for create.
 */
type PasoControlFormGroupInput = IPasoControl | PartialWithRequiredKeyOf<NewPasoControl>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends IPasoControl | NewPasoControl> = Omit<T, 'fechaHora'> & {
  fechaHora?: string | null;
};

type PasoControlFormRawValue = FormValueOf<IPasoControl>;

type NewPasoControlFormRawValue = FormValueOf<NewPasoControl>;

type PasoControlFormDefaults = Pick<NewPasoControl, 'id' | 'fechaHora'>;

type PasoControlFormGroupContent = {
  id: FormControl<PasoControlFormRawValue['id'] | NewPasoControl['id']>;
  fechaHora: FormControl<PasoControlFormRawValue['fechaHora']>;
  equipo: FormControl<PasoControlFormRawValue['equipo']>;
  puntoControl: FormControl<PasoControlFormRawValue['puntoControl']>;
  validadoPor: FormControl<PasoControlFormRawValue['validadoPor']>;
};

export type PasoControlFormGroup = FormGroup<PasoControlFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class PasoControlFormService {
  createPasoControlFormGroup(pasoControl: PasoControlFormGroupInput = { id: null }): PasoControlFormGroup {
    const pasoControlRawValue = this.convertPasoControlToPasoControlRawValue({
      ...this.getFormDefaults(),
      ...pasoControl,
    });
    return new FormGroup<PasoControlFormGroupContent>({
      id: new FormControl(
        { value: pasoControlRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      fechaHora: new FormControl(pasoControlRawValue.fechaHora),
      equipo: new FormControl(pasoControlRawValue.equipo),
      puntoControl: new FormControl(pasoControlRawValue.puntoControl),
      validadoPor: new FormControl(pasoControlRawValue.validadoPor),
    });
  }

  getPasoControl(form: PasoControlFormGroup): IPasoControl | NewPasoControl {
    return this.convertPasoControlRawValueToPasoControl(form.getRawValue() as PasoControlFormRawValue | NewPasoControlFormRawValue);
  }

  resetForm(form: PasoControlFormGroup, pasoControl: PasoControlFormGroupInput): void {
    const pasoControlRawValue = this.convertPasoControlToPasoControlRawValue({ ...this.getFormDefaults(), ...pasoControl });
    form.reset(
      {
        ...pasoControlRawValue,
        id: { value: pasoControlRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): PasoControlFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      fechaHora: currentTime,
    };
  }

  private convertPasoControlRawValueToPasoControl(
    rawPasoControl: PasoControlFormRawValue | NewPasoControlFormRawValue
  ): IPasoControl | NewPasoControl {
    return {
      ...rawPasoControl,
      fechaHora: dayjs(rawPasoControl.fechaHora, DATE_TIME_FORMAT),
    };
  }

  private convertPasoControlToPasoControlRawValue(
    pasoControl: IPasoControl | (Partial<NewPasoControl> & PasoControlFormDefaults)
  ): PasoControlFormRawValue | PartialWithRequiredKeyOf<NewPasoControlFormRawValue> {
    return {
      ...pasoControl,
      fechaHora: pasoControl.fechaHora ? pasoControl.fechaHora.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
