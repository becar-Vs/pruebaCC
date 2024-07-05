import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe } from 'app/shared/date';
import { ISevUnidadNegocio } from '../sev-unidad-negocio.model';

@Component({
  standalone: true,
  selector: 'jhi-sev-unidad-negocio-detail',
  templateUrl: './sev-unidad-negocio-detail.component.html',
  imports: [SharedModule, RouterModule, DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe],
})
export class SevUnidadNegocioDetailComponent {
  sevUnidadNegocio = input<ISevUnidadNegocio | null>(null);

  previousState(): void {
    window.history.back();
  }
}
