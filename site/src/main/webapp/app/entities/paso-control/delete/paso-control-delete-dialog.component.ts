import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IPasoControl } from '../paso-control.model';
import { PasoControlService } from '../service/paso-control.service';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';

@Component({
  templateUrl: './paso-control-delete-dialog.component.html',
})
export class PasoControlDeleteDialogComponent {
  pasoControl?: IPasoControl;

  constructor(protected pasoControlService: PasoControlService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.pasoControlService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
