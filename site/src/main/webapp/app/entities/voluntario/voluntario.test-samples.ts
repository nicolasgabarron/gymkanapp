import dayjs from 'dayjs/esm';

import { IVoluntario, NewVoluntario } from './voluntario.model';

export const sampleWithRequiredData: IVoluntario = {
  id: 33087,
};

export const sampleWithPartialData: IVoluntario = {
  id: 251,
  nombre: 'connect',
  apellidos: 'Raton bypassing revolutionary',
};

export const sampleWithFullData: IVoluntario = {
  id: 33654,
  dni: 'Marroquinería',
  nombre: 'program Account COM',
  apellidos: 'Bedfordshire asimétrica Ergonómico',
  fechaNacimiento: dayjs('2022-10-31'),
};

export const sampleWithNewData: NewVoluntario = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
