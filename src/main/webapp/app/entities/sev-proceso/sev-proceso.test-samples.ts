import { ISevProceso, NewSevProceso } from './sev-proceso.model';

export const sampleWithRequiredData: ISevProceso = {
  id: 18307,
  idProceso: 26342,
  nombre: 'queasily huzzah',
  fkIdResponsable: 31130,
};

export const sampleWithPartialData: ISevProceso = {
  id: 32169,
  idProceso: 3474,
  nombre: 'meh',
  fkIdResponsable: 7082,
};

export const sampleWithFullData: ISevProceso = {
  id: 6871,
  idProceso: 9705,
  nombre: 'ack',
  fkIdResponsable: 21137,
};

export const sampleWithNewData: NewSevProceso = {
  idProceso: 5640,
  nombre: 'regarding',
  fkIdResponsable: 29176,
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
