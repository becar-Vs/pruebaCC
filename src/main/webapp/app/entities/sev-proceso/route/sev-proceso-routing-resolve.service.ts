import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of, EMPTY, Observable } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ISevProceso } from '../sev-proceso.model';
import { SevProcesoService } from '../service/sev-proceso.service';

const sevProcesoResolve = (route: ActivatedRouteSnapshot): Observable<null | ISevProceso> => {
  const id = route.params['id'];
  if (id) {
    return inject(SevProcesoService)
      .find(id)
      .pipe(
        mergeMap((sevProceso: HttpResponse<ISevProceso>) => {
          if (sevProceso.body) {
            return of(sevProceso.body);
          } else {
            inject(Router).navigate(['404']);
            return EMPTY;
          }
        }),
      );
  }
  return of(null);
};

export default sevProcesoResolve;
