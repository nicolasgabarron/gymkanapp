import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IPuntoControl } from '../punto-control.model';
import { PuntoControlService } from '../service/punto-control.service';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';

@Component({
  templateUrl: './punto-control-delete-dialog.component.html',
})
export class PuntoControlDeleteDialogComponent {
  puntoControl?: IPuntoControl;

  constructor(protected puntoControlService: PuntoControlService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.puntoControlService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
