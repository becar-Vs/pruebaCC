import { ISevProceso } from 'app/entities/sev-proceso/sev-proceso.model';

export interface ISevGrafica {
  id: number;
  idGrafica?: number | null;
  nombre?: string | null;
  fkIdResponsable?: number | null;
  proceso?: ISevProceso | null;
}

export type NewSevGrafica = Omit<ISevGrafica, 'id'> & { id: null };
