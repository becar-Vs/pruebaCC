import dayjs from 'dayjs/esm';
import { ISevGrafica } from 'app/entities/sev-grafica/sev-grafica.model';

export interface ISevGraficaData {
  id: number;
  idRow?: number | null;
  fechaObjetivo?: dayjs.Dayjs | null;
  valorObjetivo?: number | null;
  valorLogrado?: number | null;
  grafica?: ISevGrafica | null;
}

export type NewSevGraficaData = Omit<ISevGraficaData, 'id'> & { id: null };
