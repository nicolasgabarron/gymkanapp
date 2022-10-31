import dayjs from 'dayjs/esm';

import { IParticipante, NewParticipante } from './participante.model';

export const sampleWithRequiredData: IParticipante = {
  id: 67295,
};

export const sampleWithPartialData: IParticipante = {
  id: 32383,
  nombre: 'Madera Corporativo',
  fechaNacimiento: dayjs('2022-10-31'),
};

export const sampleWithFullData: IParticipante = {
  id: 10887,
  dni: 'Colombia Kong Rojo',
  nombre: 'Región',
  apellidos: 'iniciativa SSL',
  fechaNacimiento: dayjs('2022-10-31'),
};

export const sampleWithNewData: NewParticipante = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
