import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import { SevUnidadNegocioComponent } from './list/sev-unidad-negocio.component';
import { SevUnidadNegocioDetailComponent } from './detail/sev-unidad-negocio-detail.component';
import { SevUnidadNegocioUpdateComponent } from './update/sev-unidad-negocio-update.component';
import SevUnidadNegocioResolve from './route/sev-unidad-negocio-routing-resolve.service';

const sevUnidadNegocioRoute: Routes = [
  {
    path: '',
    component: SevUnidadNegocioComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: SevUnidadNegocioDetailComponent,
    resolve: {
      sevUnidadNegocio: SevUnidadNegocioResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: SevUnidadNegocioUpdateComponent,
    resolve: {
      sevUnidadNegocio: SevUnidadNegocioResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: SevUnidadNegocioUpdateComponent,
    resolve: {
      sevUnidadNegocio: SevUnidadNegocioResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default sevUnidadNegocioRoute;
