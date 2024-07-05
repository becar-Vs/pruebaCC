import { inject, Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ISevGrafica, NewSevGrafica } from '../sev-grafica.model';

export type PartialUpdateSevGrafica = Partial<ISevGrafica> & Pick<ISevGrafica, 'id'>;

export type EntityResponseType = HttpResponse<ISevGrafica>;
export type EntityArrayResponseType = HttpResponse<ISevGrafica[]>;

@Injectable({ providedIn: 'root' })
export class SevGraficaService {
  protected http = inject(HttpClient);
  protected applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/sev-graficas');

  create(sevGrafica: NewSevGrafica): Observable<EntityResponseType> {
    return this.http.post<ISevGrafica>(this.resourceUrl, sevGrafica, { observe: 'response' });
  }

  update(sevGrafica: ISevGrafica): Observable<EntityResponseType> {
    return this.http.put<ISevGrafica>(`${this.resourceUrl}/${this.getSevGraficaIdentifier(sevGrafica)}`, sevGrafica, {
      observe: 'response',
    });
  }

  partialUpdate(sevGrafica: PartialUpdateSevGrafica): Observable<EntityResponseType> {
    return this.http.patch<ISevGrafica>(`${this.resourceUrl}/${this.getSevGraficaIdentifier(sevGrafica)}`, sevGrafica, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<ISevGrafica>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ISevGrafica[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getSevGraficaIdentifier(sevGrafica: Pick<ISevGrafica, 'id'>): number {
    return sevGrafica.id;
  }

  compareSevGrafica(o1: Pick<ISevGrafica, 'id'> | null, o2: Pick<ISevGrafica, 'id'> | null): boolean {
    return o1 && o2 ? this.getSevGraficaIdentifier(o1) === this.getSevGraficaIdentifier(o2) : o1 === o2;
  }

  addSevGraficaToCollectionIfMissing<Type extends Pick<ISevGrafica, 'id'>>(
    sevGraficaCollection: Type[],
    ...sevGraficasToCheck: (Type | null | undefined)[]
  ): Type[] {
    const sevGraficas: Type[] = sevGraficasToCheck.filter(isPresent);
    if (sevGraficas.length > 0) {
      const sevGraficaCollectionIdentifiers = sevGraficaCollection.map(sevGraficaItem => this.getSevGraficaIdentifier(sevGraficaItem));
      const sevGraficasToAdd = sevGraficas.filter(sevGraficaItem => {
        const sevGraficaIdentifier = this.getSevGraficaIdentifier(sevGraficaItem);
        if (sevGraficaCollectionIdentifiers.includes(sevGraficaIdentifier)) {
          return false;
        }
        sevGraficaCollectionIdentifiers.push(sevGraficaIdentifier);
        return true;
      });
      return [...sevGraficasToAdd, ...sevGraficaCollection];
    }
    return sevGraficaCollection;
  }
}
