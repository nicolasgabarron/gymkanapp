import { AutoCompleteModule } from 'primeng/autocomplete';
import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { PasoControlComponent } from './list/paso-control.component';
import { PasoControlDetailComponent } from './detail/paso-control-detail.component';
import { PasoControlUpdateComponent } from './update/paso-control-update.component';
import { PasoControlDeleteDialogComponent } from './delete/paso-control-delete-dialog.component';
import { PasoControlRoutingModule } from './route/paso-control-routing.module';

@NgModule({
  imports: [SharedModule, PasoControlRoutingModule, AutoCompleteModule],
  declarations: [PasoControlComponent, PasoControlDetailComponent, PasoControlUpdateComponent, PasoControlDeleteDialogComponent],
})
export class PasoControlModule {}
