import dayjs from 'dayjs/esm';

import { ISevGraficaData, NewSevGraficaData } from './sev-grafica-data.model';

export const sampleWithRequiredData: ISevGraficaData = {
  id: 7550,
  idRow: 32739,
  fechaObjetivo: dayjs('2024-07-04'),
  valorObjetivo: 8163.91,
  valorLogrado: 30795.65,
};

export const sampleWithPartialData: ISevGraficaData = {
  id: 13880,
  idRow: 28339,
  fechaObjetivo: dayjs('2024-07-04'),
  valorObjetivo: 32347.16,
  valorLogrado: 30680.63,
};

export const sampleWithFullData: ISevGraficaData = {
  id: 24521,
  idRow: 19011,
  fechaObjetivo: dayjs('2024-07-04'),
  valorObjetivo: 11294.09,
  valorLogrado: 8528.38,
};

export const sampleWithNewData: NewSevGraficaData = {
  idRow: 25433,
  fechaObjetivo: dayjs('2024-07-04'),
  valorObjetivo: 25780.92,
  valorLogrado: 6961.7,
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
