import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { ISevGrafica } from '../sev-grafica.model';
import { SevGraficaService } from '../service/sev-grafica.service';

@Component({
  standalone: true,
  templateUrl: './sev-grafica-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class SevGraficaDeleteDialogComponent {
  sevGrafica?: ISevGrafica;

  protected sevGraficaService = inject(SevGraficaService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.sevGraficaService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
