export interface ISevUnidadNegocio {
  id: number;
  idUnidadNegocio?: number | null;
  nombre?: string | null;
  fkIdResponsable?: number | null;
}

export type NewSevUnidadNegocio = Omit<ISevUnidadNegocio, 'id'> & { id: null };
