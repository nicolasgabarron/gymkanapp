import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [
    RouterModule.forChild([
      {
        path: 'voluntario',
        data: { pageTitle: 'gymkanappApp.voluntario.home.title' },
        loadChildren: () => import('./voluntario/voluntario.module').then(m => m.VoluntarioModule),
      },
      {
        path: 'participante',
        data: { pageTitle: 'gymkanappApp.participante.home.title' },
        loadChildren: () => import('./participante/participante.module').then(m => m.ParticipanteModule),
      },
      {
        path: 'equipo',
        data: { pageTitle: 'gymkanappApp.equipo.home.title' },
        loadChildren: () => import('./equipo/equipo.module').then(m => m.EquipoModule),
      },
      {
        path: 'punto-control',
        data: { pageTitle: 'gymkanappApp.puntoControl.home.title' },
        loadChildren: () => import('./punto-control/punto-control.module').then(m => m.PuntoControlModule),
      },
      {
        path: 'paso-control',
        data: { pageTitle: 'gymkanappApp.pasoControl.home.title' },
        loadChildren: () => import('./paso-control/paso-control.module').then(m => m.PasoControlModule),
      },
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ]),
  ],
})
export class EntityRoutingModule {}
