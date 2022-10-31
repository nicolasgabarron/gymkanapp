import dayjs from 'dayjs/esm';

import { IPasoControl, NewPasoControl } from './paso-control.model';

export const sampleWithRequiredData: IPasoControl = {
  id: 65159,
};

export const sampleWithPartialData: IPasoControl = {
  id: 25479,
};

export const sampleWithFullData: IPasoControl = {
  id: 82788,
  fechaHora: dayjs('2022-10-30T11:15'),
};

export const sampleWithNewData: NewPasoControl = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
