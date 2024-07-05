import { ISevUnidadNegocio, NewSevUnidadNegocio } from './sev-unidad-negocio.model';

export const sampleWithRequiredData: ISevUnidadNegocio = {
  id: 21091,
  idUnidadNegocio: 24276,
  nombre: 'afterwards pace searchingly',
  fkIdResponsable: 21391,
};

export const sampleWithPartialData: ISevUnidadNegocio = {
  id: 1957,
  idUnidadNegocio: 11778,
  nombre: 'eek grave',
  fkIdResponsable: 17567,
};

export const sampleWithFullData: ISevUnidadNegocio = {
  id: 6714,
  idUnidadNegocio: 17757,
  nombre: 'reinvigorate',
  fkIdResponsable: 1167,
};

export const sampleWithNewData: NewSevUnidadNegocio = {
  idUnidadNegocio: 6116,
  nombre: 'whip',
  fkIdResponsable: 30748,
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
