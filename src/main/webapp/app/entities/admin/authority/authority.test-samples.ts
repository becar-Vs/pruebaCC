import { IAuthority, NewAuthority } from './authority.model';

export const sampleWithRequiredData: IAuthority = {
  name: 'd76fa307-3240-49c8-b738-ba6613a5cd00',
};

export const sampleWithPartialData: IAuthority = {
  name: 'a0cfbcd2-d9bc-4adb-8c29-4e1436056d53',
};

export const sampleWithFullData: IAuthority = {
  name: 'd4472c0f-43ac-4e49-b4a3-2e5373221a01',
};

export const sampleWithNewData: NewAuthority = {
  name: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
