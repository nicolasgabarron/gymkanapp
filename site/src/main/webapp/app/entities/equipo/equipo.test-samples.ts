import { IEquipo, NewEquipo } from './equipo.model';

export const sampleWithRequiredData: IEquipo = {
  id: 58929,
  cantidadIntegrantes: 73739,
};

export const sampleWithPartialData: IEquipo = {
  id: 15919,
  nombre: 'función',
  cantidadIntegrantes: 13393,
};

export const sampleWithFullData: IEquipo = {
  id: 79202,
  identificador: 'complejidad Morado Universal',
  nombre: 'Tunez sensor Senior',
  cantidadIntegrantes: 90413,
};

export const sampleWithNewData: NewEquipo = {
  cantidadIntegrantes: 31055,
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
