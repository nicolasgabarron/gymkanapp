import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { EquipoComponent } from './list/equipo.component';
import { EquipoDetailComponent } from './detail/equipo-detail.component';
import { EquipoUpdateComponent } from './update/equipo-update.component';
import { EquipoDeleteDialogComponent } from './delete/equipo-delete-dialog.component';
import { EquipoRoutingModule } from './route/equipo-routing.module';

@NgModule({
  imports: [SharedModule, EquipoRoutingModule],
  declarations: [EquipoComponent, EquipoDetailComponent, EquipoUpdateComponent, EquipoDeleteDialogComponent],
})
export class EquipoModule {}
