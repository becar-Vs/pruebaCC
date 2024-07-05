import { inject, Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { map, Observable } from 'rxjs';

import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { DATE_FORMAT } from 'app/config/input.constants';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ISevGraficaData, NewSevGraficaData } from '../sev-grafica-data.model';

export type PartialUpdateSevGraficaData = Partial<ISevGraficaData> & Pick<ISevGraficaData, 'id'>;

type RestOf<T extends ISevGraficaData | NewSevGraficaData> = Omit<T, 'fechaObjetivo'> & {
  fechaObjetivo?: string | null;
};

export type RestSevGraficaData = RestOf<ISevGraficaData>;

export type NewRestSevGraficaData = RestOf<NewSevGraficaData>;

export type PartialUpdateRestSevGraficaData = RestOf<PartialUpdateSevGraficaData>;

export type EntityResponseType = HttpResponse<ISevGraficaData>;
export type EntityArrayResponseType = HttpResponse<ISevGraficaData[]>;

@Injectable({ providedIn: 'root' })
export class SevGraficaDataService {
  protected http = inject(HttpClient);
  protected applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/sev-grafica-data');

  create(sevGraficaData: NewSevGraficaData): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(sevGraficaData);
    return this.http
      .post<RestSevGraficaData>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(sevGraficaData: ISevGraficaData): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(sevGraficaData);
    return this.http
      .put<RestSevGraficaData>(`${this.resourceUrl}/${this.getSevGraficaDataIdentifier(sevGraficaData)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(sevGraficaData: PartialUpdateSevGraficaData): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(sevGraficaData);
    return this.http
      .patch<RestSevGraficaData>(`${this.resourceUrl}/${this.getSevGraficaDataIdentifier(sevGraficaData)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestSevGraficaData>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestSevGraficaData[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getSevGraficaDataIdentifier(sevGraficaData: Pick<ISevGraficaData, 'id'>): number {
    return sevGraficaData.id;
  }

  compareSevGraficaData(o1: Pick<ISevGraficaData, 'id'> | null, o2: Pick<ISevGraficaData, 'id'> | null): boolean {
    return o1 && o2 ? this.getSevGraficaDataIdentifier(o1) === this.getSevGraficaDataIdentifier(o2) : o1 === o2;
  }

  addSevGraficaDataToCollectionIfMissing<Type extends Pick<ISevGraficaData, 'id'>>(
    sevGraficaDataCollection: Type[],
    ...sevGraficaDataToCheck: (Type | null | undefined)[]
  ): Type[] {
    const sevGraficaData: Type[] = sevGraficaDataToCheck.filter(isPresent);
    if (sevGraficaData.length > 0) {
      const sevGraficaDataCollectionIdentifiers = sevGraficaDataCollection.map(sevGraficaDataItem =>
        this.getSevGraficaDataIdentifier(sevGraficaDataItem),
      );
      const sevGraficaDataToAdd = sevGraficaData.filter(sevGraficaDataItem => {
        const sevGraficaDataIdentifier = this.getSevGraficaDataIdentifier(sevGraficaDataItem);
        if (sevGraficaDataCollectionIdentifiers.includes(sevGraficaDataIdentifier)) {
          return false;
        }
        sevGraficaDataCollectionIdentifiers.push(sevGraficaDataIdentifier);
        return true;
      });
      return [...sevGraficaDataToAdd, ...sevGraficaDataCollection];
    }
    return sevGraficaDataCollection;
  }

  protected convertDateFromClient<T extends ISevGraficaData | NewSevGraficaData | PartialUpdateSevGraficaData>(
    sevGraficaData: T,
  ): RestOf<T> {
    return {
      ...sevGraficaData,
      fechaObjetivo: sevGraficaData.fechaObjetivo?.format(DATE_FORMAT) ?? null,
    };
  }

  protected convertDateFromServer(restSevGraficaData: RestSevGraficaData): ISevGraficaData {
    return {
      ...restSevGraficaData,
      fechaObjetivo: restSevGraficaData.fechaObjetivo ? dayjs(restSevGraficaData.fechaObjetivo) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestSevGraficaData>): HttpResponse<ISevGraficaData> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestSevGraficaData[]>): HttpResponse<ISevGraficaData[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
