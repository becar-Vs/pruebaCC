import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import { SevGraficaDataComponent } from './list/sev-grafica-data.component';
import { SevGraficaDataDetailComponent } from './detail/sev-grafica-data-detail.component';
import { SevGraficaDataUpdateComponent } from './update/sev-grafica-data-update.component';
import SevGraficaDataResolve from './route/sev-grafica-data-routing-resolve.service';

const sevGraficaDataRoute: Routes = [
  {
    path: '',
    component: SevGraficaDataComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: SevGraficaDataDetailComponent,
    resolve: {
      sevGraficaData: SevGraficaDataResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: SevGraficaDataUpdateComponent,
    resolve: {
      sevGraficaData: SevGraficaDataResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: SevGraficaDataUpdateComponent,
    resolve: {
      sevGraficaData: SevGraficaDataResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default sevGraficaDataRoute;
