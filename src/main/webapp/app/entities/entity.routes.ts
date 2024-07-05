import { Routes } from '@angular/router';

const routes: Routes = [
  {
    path: 'authority',
    data: { pageTitle: 'pruebaApp.adminAuthority.home.title' },
    loadChildren: () => import('./admin/authority/authority.routes'),
  },
  {
    path: 'sev-unidad-negocio',
    data: { pageTitle: 'pruebaApp.sevUnidadNegocio.home.title' },
    loadChildren: () => import('./sev-unidad-negocio/sev-unidad-negocio.routes'),
  },
  {
    path: 'sev-proceso',
    data: { pageTitle: 'pruebaApp.sevProceso.home.title' },
    loadChildren: () => import('./sev-proceso/sev-proceso.routes'),
  },
  {
    path: 'sev-grafica',
    data: { pageTitle: 'pruebaApp.sevGrafica.home.title' },
    loadChildren: () => import('./sev-grafica/sev-grafica.routes'),
  },
  {
    path: 'sev-grafica-data',
    data: { pageTitle: 'pruebaApp.sevGraficaData.home.title' },
    loadChildren: () => import('./sev-grafica-data/sev-grafica-data.routes'),
  },
  /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
];

export default routes;
