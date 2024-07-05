import { ISevGrafica, NewSevGrafica } from './sev-grafica.model';

export const sampleWithRequiredData: ISevGrafica = {
  id: 5604,
  idGrafica: 30383,
  nombre: 'creepy abound',
  fkIdResponsable: 7003,
};

export const sampleWithPartialData: ISevGrafica = {
  id: 7445,
  idGrafica: 16264,
  nombre: 'slur carefully',
  fkIdResponsable: 23748,
};

export const sampleWithFullData: ISevGrafica = {
  id: 126,
  idGrafica: 4929,
  nombre: 'gosh yearly',
  fkIdResponsable: 14845,
};

export const sampleWithNewData: NewSevGrafica = {
  idGrafica: 17943,
  nombre: 'unabashedly given',
  fkIdResponsable: 931,
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
