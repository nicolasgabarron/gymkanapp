import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { PasoControlComponent } from '../list/paso-control.component';
import { PasoControlDetailComponent } from '../detail/paso-control-detail.component';
import { PasoControlUpdateComponent } from '../update/paso-control-update.component';
import { PasoControlRoutingResolveService } from './paso-control-routing-resolve.service';
import { ASC } from 'app/config/navigation.constants';

const pasoControlRoute: Routes = [
  {
    path: '',
    component: PasoControlComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: PasoControlDetailComponent,
    resolve: {
      pasoControl: PasoControlRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: PasoControlUpdateComponent,
    resolve: {
      pasoControl: PasoControlRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: PasoControlUpdateComponent,
    resolve: {
      pasoControl: PasoControlRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(pasoControlRoute)],
  exports: [RouterModule],
})
export class PasoControlRoutingModule {}
