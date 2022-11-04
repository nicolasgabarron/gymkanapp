import { IPuntoControl, NewPuntoControl } from './punto-control.model';

export const sampleWithRequiredData: IPuntoControl = {
  id: 6978,
};

export const sampleWithPartialData: IPuntoControl = {
  id: 18223,
  identificador: 'Orgánico',
  orden: 18215,
};

export const sampleWithFullData: IPuntoControl = {
  id: 26689,
  identificador: 'Bedfordshire Coordinador',
  orden: 84196,
  nombre: 'Morado Buckinghamshire',
  direccion: 'Azul Solar',
};

export const sampleWithNewData: NewPuntoControl = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
