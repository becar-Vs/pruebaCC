import { IUser } from './user.model';

export const sampleWithRequiredData: IUser = {
  id: 32026,
  login: 'LFlq_W',
};

export const sampleWithPartialData: IUser = {
  id: 19786,
  login: 'ZsZLz@EqdP',
};

export const sampleWithFullData: IUser = {
  id: 11123,
  login: 'r^}@p4Ny',
};
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
