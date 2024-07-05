import { ISevUnidadNegocio } from 'app/entities/sev-unidad-negocio/sev-unidad-negocio.model';

export interface ISevProceso {
  id: number;
  idProceso?: number | null;
  nombre?: string | null;
  fkIdResponsable?: number | null;
  unidadNegocio?: ISevUnidadNegocio | null;
}

export type NewSevProceso = Omit<ISevProceso, 'id'> & { id: null };
