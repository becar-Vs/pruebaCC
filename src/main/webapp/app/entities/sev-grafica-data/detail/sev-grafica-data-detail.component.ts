import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe } from 'app/shared/date';
import { ISevGraficaData } from '../sev-grafica-data.model';

@Component({
  standalone: true,
  selector: 'jhi-sev-grafica-data-detail',
  templateUrl: './sev-grafica-data-detail.component.html',
  imports: [SharedModule, RouterModule, DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe],
})
export class SevGraficaDataDetailComponent {
  sevGraficaData = input<ISevGraficaData | null>(null);

  previousState(): void {
    window.history.back();
  }
}
