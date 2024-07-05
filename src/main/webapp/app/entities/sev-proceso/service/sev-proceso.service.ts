import { inject, Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ISevProceso, NewSevProceso } from '../sev-proceso.model';

export type PartialUpdateSevProceso = Partial<ISevProceso> & Pick<ISevProceso, 'id'>;

export type EntityResponseType = HttpResponse<ISevProceso>;
export type EntityArrayResponseType = HttpResponse<ISevProceso[]>;

@Injectable({ providedIn: 'root' })
export class SevProcesoService {
  protected http = inject(HttpClient);
  protected applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/sev-procesos');

  create(sevProceso: NewSevProceso): Observable<EntityResponseType> {
    return this.http.post<ISevProceso>(this.resourceUrl, sevProceso, { observe: 'response' });
  }

  update(sevProceso: ISevProceso): Observable<EntityResponseType> {
    return this.http.put<ISevProceso>(`${this.resourceUrl}/${this.getSevProcesoIdentifier(sevProceso)}`, sevProceso, {
      observe: 'response',
    });
  }

  partialUpdate(sevProceso: PartialUpdateSevProceso): Observable<EntityResponseType> {
    return this.http.patch<ISevProceso>(`${this.resourceUrl}/${this.getSevProcesoIdentifier(sevProceso)}`, sevProceso, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<ISevProceso>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ISevProceso[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getSevProcesoIdentifier(sevProceso: Pick<ISevProceso, 'id'>): number {
    return sevProceso.id;
  }

  compareSevProceso(o1: Pick<ISevProceso, 'id'> | null, o2: Pick<ISevProceso, 'id'> | null): boolean {
    return o1 && o2 ? this.getSevProcesoIdentifier(o1) === this.getSevProcesoIdentifier(o2) : o1 === o2;
  }

  addSevProcesoToCollectionIfMissing<Type extends Pick<ISevProceso, 'id'>>(
    sevProcesoCollection: Type[],
    ...sevProcesosToCheck: (Type | null | undefined)[]
  ): Type[] {
    const sevProcesos: Type[] = sevProcesosToCheck.filter(isPresent);
    if (sevProcesos.length > 0) {
      const sevProcesoCollectionIdentifiers = sevProcesoCollection.map(sevProcesoItem => this.getSevProcesoIdentifier(sevProcesoItem));
      const sevProcesosToAdd = sevProcesos.filter(sevProcesoItem => {
        const sevProcesoIdentifier = this.getSevProcesoIdentifier(sevProcesoItem);
        if (sevProcesoCollectionIdentifiers.includes(sevProcesoIdentifier)) {
          return false;
        }
        sevProcesoCollectionIdentifiers.push(sevProcesoIdentifier);
        return true;
      });
      return [...sevProcesosToAdd, ...sevProcesoCollection];
    }
    return sevProcesoCollection;
  }
}
