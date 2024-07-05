import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of, EMPTY, Observable } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ISevGrafica } from '../sev-grafica.model';
import { SevGraficaService } from '../service/sev-grafica.service';

const sevGraficaResolve = (route: ActivatedRouteSnapshot): Observable<null | ISevGrafica> => {
  const id = route.params['id'];
  if (id) {
    return inject(SevGraficaService)
      .find(id)
      .pipe(
        mergeMap((sevGrafica: HttpResponse<ISevGrafica>) => {
          if (sevGrafica.body) {
            return of(sevGrafica.body);
          } else {
            inject(Router).navigate(['404']);
            return EMPTY;
          }
        }),
      );
  }
  return of(null);
};

export default sevGraficaResolve;
