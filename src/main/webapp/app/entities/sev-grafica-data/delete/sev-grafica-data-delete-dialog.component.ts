import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { ISevGraficaData } from '../sev-grafica-data.model';
import { SevGraficaDataService } from '../service/sev-grafica-data.service';

@Component({
  standalone: true,
  templateUrl: './sev-grafica-data-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class SevGraficaDataDeleteDialogComponent {
  sevGraficaData?: ISevGraficaData;

  protected sevGraficaDataService = inject(SevGraficaDataService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.sevGraficaDataService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
