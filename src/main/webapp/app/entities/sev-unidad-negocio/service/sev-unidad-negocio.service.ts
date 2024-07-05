import { inject, Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ISevUnidadNegocio, NewSevUnidadNegocio } from '../sev-unidad-negocio.model';

export type PartialUpdateSevUnidadNegocio = Partial<ISevUnidadNegocio> & Pick<ISevUnidadNegocio, 'id'>;

export type EntityResponseType = HttpResponse<ISevUnidadNegocio>;
export type EntityArrayResponseType = HttpResponse<ISevUnidadNegocio[]>;

@Injectable({ providedIn: 'root' })
export class SevUnidadNegocioService {
  protected http = inject(HttpClient);
  protected applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/sev-unidad-negocios');

  create(sevUnidadNegocio: NewSevUnidadNegocio): Observable<EntityResponseType> {
    return this.http.post<ISevUnidadNegocio>(this.resourceUrl, sevUnidadNegocio, { observe: 'response' });
  }

  update(sevUnidadNegocio: ISevUnidadNegocio): Observable<EntityResponseType> {
    return this.http.put<ISevUnidadNegocio>(
      `${this.resourceUrl}/${this.getSevUnidadNegocioIdentifier(sevUnidadNegocio)}`,
      sevUnidadNegocio,
      { observe: 'response' },
    );
  }

  partialUpdate(sevUnidadNegocio: PartialUpdateSevUnidadNegocio): Observable<EntityResponseType> {
    return this.http.patch<ISevUnidadNegocio>(
      `${this.resourceUrl}/${this.getSevUnidadNegocioIdentifier(sevUnidadNegocio)}`,
      sevUnidadNegocio,
      { observe: 'response' },
    );
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<ISevUnidadNegocio>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ISevUnidadNegocio[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getSevUnidadNegocioIdentifier(sevUnidadNegocio: Pick<ISevUnidadNegocio, 'id'>): number {
    return sevUnidadNegocio.id;
  }

  compareSevUnidadNegocio(o1: Pick<ISevUnidadNegocio, 'id'> | null, o2: Pick<ISevUnidadNegocio, 'id'> | null): boolean {
    return o1 && o2 ? this.getSevUnidadNegocioIdentifier(o1) === this.getSevUnidadNegocioIdentifier(o2) : o1 === o2;
  }

  addSevUnidadNegocioToCollectionIfMissing<Type extends Pick<ISevUnidadNegocio, 'id'>>(
    sevUnidadNegocioCollection: Type[],
    ...sevUnidadNegociosToCheck: (Type | null | undefined)[]
  ): Type[] {
    const sevUnidadNegocios: Type[] = sevUnidadNegociosToCheck.filter(isPresent);
    if (sevUnidadNegocios.length > 0) {
      const sevUnidadNegocioCollectionIdentifiers = sevUnidadNegocioCollection.map(sevUnidadNegocioItem =>
        this.getSevUnidadNegocioIdentifier(sevUnidadNegocioItem),
      );
      const sevUnidadNegociosToAdd = sevUnidadNegocios.filter(sevUnidadNegocioItem => {
        const sevUnidadNegocioIdentifier = this.getSevUnidadNegocioIdentifier(sevUnidadNegocioItem);
        if (sevUnidadNegocioCollectionIdentifiers.includes(sevUnidadNegocioIdentifier)) {
          return false;
        }
        sevUnidadNegocioCollectionIdentifiers.push(sevUnidadNegocioIdentifier);
        return true;
      });
      return [...sevUnidadNegociosToAdd, ...sevUnidadNegocioCollection];
    }
    return sevUnidadNegocioCollection;
  }
}
