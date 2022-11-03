import { AutoCompleteModule } from 'primeng/autocomplete';
import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { VoluntarioComponent } from './list/voluntario.component';
import { VoluntarioDetailComponent } from './detail/voluntario-detail.component';
import { VoluntarioUpdateComponent } from './update/voluntario-update.component';
import { VoluntarioDeleteDialogComponent } from './delete/voluntario-delete-dialog.component';
import { VoluntarioRoutingModule } from './route/voluntario-routing.module';


@NgModule({
  imports: [SharedModule, VoluntarioRoutingModule, AutoCompleteModule],
  declarations: [VoluntarioComponent, VoluntarioDetailComponent, VoluntarioUpdateComponent, VoluntarioDeleteDialogComponent],
})
export class VoluntarioModule {}
