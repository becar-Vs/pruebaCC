import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import { SevGraficaComponent } from './list/sev-grafica.component';
import { SevGraficaDetailComponent } from './detail/sev-grafica-detail.component';
import { SevGraficaUpdateComponent } from './update/sev-grafica-update.component';
import SevGraficaResolve from './route/sev-grafica-routing-resolve.service';

const sevGraficaRoute: Routes = [
  {
    path: '',
    component: SevGraficaComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: SevGraficaDetailComponent,
    resolve: {
      sevGrafica: SevGraficaResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: SevGraficaUpdateComponent,
    resolve: {
      sevGrafica: SevGraficaResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: SevGraficaUpdateComponent,
    resolve: {
      sevGrafica: SevGraficaResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default sevGraficaRoute;
