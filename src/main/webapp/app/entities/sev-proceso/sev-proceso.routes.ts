import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import { SevProcesoComponent } from './list/sev-proceso.component';
import { SevProcesoDetailComponent } from './detail/sev-proceso-detail.component';
import { SevProcesoUpdateComponent } from './update/sev-proceso-update.component';
import SevProcesoResolve from './route/sev-proceso-routing-resolve.service';

const sevProcesoRoute: Routes = [
  {
    path: '',
    component: SevProcesoComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: SevProcesoDetailComponent,
    resolve: {
      sevProceso: SevProcesoResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: SevProcesoUpdateComponent,
    resolve: {
      sevProceso: SevProcesoResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: SevProcesoUpdateComponent,
    resolve: {
      sevProceso: SevProcesoResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default sevProcesoRoute;
