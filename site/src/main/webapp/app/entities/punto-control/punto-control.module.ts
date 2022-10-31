import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { PuntoControlComponent } from './list/punto-control.component';
import { PuntoControlDetailComponent } from './detail/punto-control-detail.component';
import { PuntoControlUpdateComponent } from './update/punto-control-update.component';
import { PuntoControlDeleteDialogComponent } from './delete/punto-control-delete-dialog.component';
import { PuntoControlRoutingModule } from './route/punto-control-routing.module';

@NgModule({
  imports: [SharedModule, PuntoControlRoutingModule],
  declarations: [PuntoControlComponent, PuntoControlDetailComponent, PuntoControlUpdateComponent, PuntoControlDeleteDialogComponent],
})
export class PuntoControlModule {}
