import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { VoluntarioComponent } from '../list/voluntario.component';
import { VoluntarioDetailComponent } from '../detail/voluntario-detail.component';
import { VoluntarioUpdateComponent } from '../update/voluntario-update.component';
import { VoluntarioRoutingResolveService } from './voluntario-routing-resolve.service';
import { ASC } from 'app/config/navigation.constants';

const voluntarioRoute: Routes = [
  {
    path: '',
    component: VoluntarioComponent,
    data: {
      defaultSort: 'apellidos,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: VoluntarioDetailComponent,
    resolve: {
      voluntario: VoluntarioRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: VoluntarioUpdateComponent,
    resolve: {
      voluntario: VoluntarioRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: VoluntarioUpdateComponent,
    resolve: {
      voluntario: VoluntarioRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(voluntarioRoute)],
  exports: [RouterModule],
})
export class VoluntarioRoutingModule {}
