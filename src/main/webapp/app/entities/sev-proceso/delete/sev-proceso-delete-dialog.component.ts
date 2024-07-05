import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { ISevProceso } from '../sev-proceso.model';
import { SevProcesoService } from '../service/sev-proceso.service';

@Component({
  standalone: true,
  templateUrl: './sev-proceso-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class SevProcesoDeleteDialogComponent {
  sevProceso?: ISevProceso;

  protected sevProcesoService = inject(SevProcesoService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.sevProcesoService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
