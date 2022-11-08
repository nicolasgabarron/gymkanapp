import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { PuntoControlComponent } from '../list/punto-control.component';
import { PuntoControlDetailComponent } from '../detail/punto-control-detail.component';
import { PuntoControlUpdateComponent } from '../update/punto-control-update.component';
import { PuntoControlRoutingResolveService } from './punto-control-routing-resolve.service';
import { ASC } from 'app/config/navigation.constants';

const puntoControlRoute: Routes = [
  {
    path: '',
    component: PuntoControlComponent,
    data: {
      defaultSort: 'orden,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: PuntoControlDetailComponent,
    resolve: {
      puntoControl: PuntoControlRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: PuntoControlUpdateComponent,
    resolve: {
      puntoControl: PuntoControlRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: PuntoControlUpdateComponent,
    resolve: {
      puntoControl: PuntoControlRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(puntoControlRoute)],
  exports: [RouterModule],
})
export class PuntoControlRoutingModule {}
