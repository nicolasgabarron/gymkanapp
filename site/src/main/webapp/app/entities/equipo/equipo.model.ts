export interface IEquipo {
  id: number;
  identificador?: string | null;
  nombre?: string | null;
  cantidadIntegrantes?: number | null;
}

export type NewEquipo = Omit<IEquipo, 'id'> & { id: null };
