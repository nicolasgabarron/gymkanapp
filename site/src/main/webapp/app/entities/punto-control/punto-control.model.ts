export interface IPuntoControl {
  id: number;
  identificador?: string | null;
  orden?: number | null;
  nombre?: string | null;
  direccion?: string | null;
}

export type NewPuntoControl = Omit<IPuntoControl, 'id'> & { id: null };
