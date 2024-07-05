import { Component, inject, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { ISevProceso } from 'app/entities/sev-proceso/sev-proceso.model';
import { SevProcesoService } from 'app/entities/sev-proceso/service/sev-proceso.service';
import { ISevGrafica } from '../sev-grafica.model';
import { SevGraficaService } from '../service/sev-grafica.service';
import { SevGraficaFormService, SevGraficaFormGroup } from './sev-grafica-form.service';

@Component({
  standalone: true,
  selector: 'jhi-sev-grafica-update',
  templateUrl: './sev-grafica-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class SevGraficaUpdateComponent implements OnInit {
  isSaving = false;
  sevGrafica: ISevGrafica | null = null;

  sevProcesosSharedCollection: ISevProceso[] = [];

  protected sevGraficaService = inject(SevGraficaService);
  protected sevGraficaFormService = inject(SevGraficaFormService);
  protected sevProcesoService = inject(SevProcesoService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: SevGraficaFormGroup = this.sevGraficaFormService.createSevGraficaFormGroup();

  compareSevProceso = (o1: ISevProceso | null, o2: ISevProceso | null): boolean => this.sevProcesoService.compareSevProceso(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ sevGrafica }) => {
      this.sevGrafica = sevGrafica;
      if (sevGrafica) {
        this.updateForm(sevGrafica);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const sevGrafica = this.sevGraficaFormService.getSevGrafica(this.editForm);
    if (sevGrafica.id !== null) {
      this.subscribeToSaveResponse(this.sevGraficaService.update(sevGrafica));
    } else {
      this.subscribeToSaveResponse(this.sevGraficaService.create(sevGrafica));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ISevGrafica>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(sevGrafica: ISevGrafica): void {
    this.sevGrafica = sevGrafica;
    this.sevGraficaFormService.resetForm(this.editForm, sevGrafica);

    this.sevProcesosSharedCollection = this.sevProcesoService.addSevProcesoToCollectionIfMissing<ISevProceso>(
      this.sevProcesosSharedCollection,
      sevGrafica.proceso,
    );
  }

  protected loadRelationshipsOptions(): void {
    this.sevProcesoService
      .query()
      .pipe(map((res: HttpResponse<ISevProceso[]>) => res.body ?? []))
      .pipe(
        map((sevProcesos: ISevProceso[]) =>
          this.sevProcesoService.addSevProcesoToCollectionIfMissing<ISevProceso>(sevProcesos, this.sevGrafica?.proceso),
        ),
      )
      .subscribe((sevProcesos: ISevProceso[]) => (this.sevProcesosSharedCollection = sevProcesos));
  }
}
