import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of, EMPTY, Observable } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ISevGraficaData } from '../sev-grafica-data.model';
import { SevGraficaDataService } from '../service/sev-grafica-data.service';

const sevGraficaDataResolve = (route: ActivatedRouteSnapshot): Observable<null | ISevGraficaData> => {
  const id = route.params['id'];
  if (id) {
    return inject(SevGraficaDataService)
      .find(id)
      .pipe(
        mergeMap((sevGraficaData: HttpResponse<ISevGraficaData>) => {
          if (sevGraficaData.body) {
            return of(sevGraficaData.body);
          } else {
            inject(Router).navigate(['404']);
            return EMPTY;
          }
        }),
      );
  }
  return of(null);
};

export default sevGraficaDataResolve;
