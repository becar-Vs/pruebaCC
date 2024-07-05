import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { ISevUnidadNegocio } from '../sev-unidad-negocio.model';
import { SevUnidadNegocioService } from '../service/sev-unidad-negocio.service';

@Component({
  standalone: true,
  templateUrl: './sev-unidad-negocio-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class SevUnidadNegocioDeleteDialogComponent {
  sevUnidadNegocio?: ISevUnidadNegocio;

  protected sevUnidadNegocioService = inject(SevUnidadNegocioService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.sevUnidadNegocioService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
