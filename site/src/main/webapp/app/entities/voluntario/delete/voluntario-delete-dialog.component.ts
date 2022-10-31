import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IVoluntario } from '../voluntario.model';
import { VoluntarioService } from '../service/voluntario.service';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';

@Component({
  templateUrl: './voluntario-delete-dialog.component.html',
})
export class VoluntarioDeleteDialogComponent {
  voluntario?: IVoluntario;

  constructor(protected voluntarioService: VoluntarioService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.voluntarioService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
