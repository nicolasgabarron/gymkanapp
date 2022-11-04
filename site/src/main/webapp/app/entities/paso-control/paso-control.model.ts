import dayjs from 'dayjs/esm';
import { IEquipo } from 'app/entities/equipo/equipo.model';
import { IPuntoControl } from 'app/entities/punto-control/punto-control.model';
import { IVoluntario } from 'app/entities/voluntario/voluntario.model';

export interface IPasoControl {
  id: number;
  fechaHora?: dayjs.Dayjs | null;
  equipo?: Pick<IEquipo, 'id' | 'identificador'> | null;
  puntoControl?: Pick<IPuntoControl, 'id' | 'nombre'> | null;
  validadoPor?: Pick<IVoluntario, 'id' | 'dni'> | null;
}

export type NewPasoControl = Omit<IPasoControl, 'id'> & { id: null };
