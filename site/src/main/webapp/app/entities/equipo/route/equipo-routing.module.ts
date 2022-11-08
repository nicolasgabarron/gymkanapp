import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { EquipoComponent } from '../list/equipo.component';
import { EquipoDetailComponent } from '../detail/equipo-detail.component';
import { EquipoUpdateComponent } from '../update/equipo-update.component';
import { EquipoRoutingResolveService } from './equipo-routing-resolve.service';
import { ASC } from 'app/config/navigation.constants';

const equipoRoute: Routes = [
  {
    path: '',
    component: EquipoComponent,
    data: {
      defaultSort: 'identificador,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: EquipoDetailComponent,
    resolve: {
      equipo: EquipoRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: EquipoUpdateComponent,
    resolve: {
      equipo: EquipoRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: EquipoUpdateComponent,
    resolve: {
      equipo: EquipoRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(equipoRoute)],
  exports: [RouterModule],
})
export class EquipoRoutingModule {}
