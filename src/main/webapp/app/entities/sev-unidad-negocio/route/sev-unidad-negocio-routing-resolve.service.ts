import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of, EMPTY, Observable } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ISevUnidadNegocio } from '../sev-unidad-negocio.model';
import { SevUnidadNegocioService } from '../service/sev-unidad-negocio.service';

const sevUnidadNegocioResolve = (route: ActivatedRouteSnapshot): Observable<null | ISevUnidadNegocio> => {
  const id = route.params['id'];
  if (id) {
    return inject(SevUnidadNegocioService)
      .find(id)
      .pipe(
        mergeMap((sevUnidadNegocio: HttpResponse<ISevUnidadNegocio>) => {
          if (sevUnidadNegocio.body) {
            return of(sevUnidadNegocio.body);
          } else {
            inject(Router).navigate(['404']);
            return EMPTY;
          }
        }),
      );
  }
  return of(null);
};

export default sevUnidadNegocioResolve;
