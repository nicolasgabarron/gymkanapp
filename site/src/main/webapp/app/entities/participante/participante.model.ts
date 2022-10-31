import dayjs from 'dayjs/esm';
import { IUser } from 'app/entities/user/user.model';
import { IEquipo } from 'app/entities/equipo/equipo.model';

export interface IParticipante {
  id: number;
  dni?: string | null;
  nombre?: string | null;
  apellidos?: string | null;
  fechaNacimiento?: dayjs.Dayjs | null;
  usuarioApp?: Pick<IUser, 'id'> | null;
  equipo?: Pick<IEquipo, 'id'> | null;
}

export type NewParticipante = Omit<IParticipante, 'id'> & { id: null };
