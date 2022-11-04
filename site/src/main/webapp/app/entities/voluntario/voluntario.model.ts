import dayjs from 'dayjs/esm';
import { IUser } from 'app/entities/user/user.model';
import { IPuntoControl } from 'app/entities/punto-control/punto-control.model';

export interface IVoluntario {
  id: number;
  dni?: string | null;
  nombre?: string | null;
  apellidos?: string | null;
  fechaNacimiento?: dayjs.Dayjs | null;
  usuarioApp?: Pick<IUser, 'id'> | null;
  puntoControl?: Pick<IPuntoControl, 'id' | 'nombre'> | null;
}

export type NewVoluntario = Omit<IVoluntario, 'id'> & { id: null };
