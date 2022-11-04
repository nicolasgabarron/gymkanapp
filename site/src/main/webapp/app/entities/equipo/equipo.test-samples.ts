import { IEquipo, NewEquipo } from './equipo.model';

export const sampleWithRequiredData: IEquipo = {
  id: 58929,
};

export const sampleWithPartialData: IEquipo = {
  id: 57141,
  identificador: 'Increible',
};

export const sampleWithFullData: IEquipo = {
  id: 70695,
  identificador: 'optical',
  nombre: 'Dinánmico',
};

export const sampleWithNewData: NewEquipo = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
