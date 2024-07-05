import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe } from 'app/shared/date';
import { ISevProceso } from '../sev-proceso.model';

@Component({
  standalone: true,
  selector: 'jhi-sev-proceso-detail',
  templateUrl: './sev-proceso-detail.component.html',
  imports: [SharedModule, RouterModule, DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe],
})
export class SevProcesoDetailComponent {
  sevProceso = input<ISevProceso | null>(null);

  previousState(): void {
    window.history.back();
  }
}
