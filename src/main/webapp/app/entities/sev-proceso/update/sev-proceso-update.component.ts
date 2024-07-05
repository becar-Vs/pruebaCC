import { Component, inject, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { ISevUnidadNegocio } from 'app/entities/sev-unidad-negocio/sev-unidad-negocio.model';
import { SevUnidadNegocioService } from 'app/entities/sev-unidad-negocio/service/sev-unidad-negocio.service';
import { ISevProceso } from '../sev-proceso.model';
import { SevProcesoService } from '../service/sev-proceso.service';
import { SevProcesoFormService, SevProcesoFormGroup } from './sev-proceso-form.service';

@Component({
  standalone: true,
  selector: 'jhi-sev-proceso-update',
  templateUrl: './sev-proceso-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class SevProcesoUpdateComponent implements OnInit {
  isSaving = false;
  sevProceso: ISevProceso | null = null;

  sevUnidadNegociosSharedCollection: ISevUnidadNegocio[] = [];

  protected sevProcesoService = inject(SevProcesoService);
  protected sevProcesoFormService = inject(SevProcesoFormService);
  protected sevUnidadNegocioService = inject(SevUnidadNegocioService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: SevProcesoFormGroup = this.sevProcesoFormService.createSevProcesoFormGroup();

  compareSevUnidadNegocio = (o1: ISevUnidadNegocio | null, o2: ISevUnidadNegocio | null): boolean =>
    this.sevUnidadNegocioService.compareSevUnidadNegocio(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ sevProceso }) => {
      this.sevProceso = sevProceso;
      if (sevProceso) {
        this.updateForm(sevProceso);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const sevProceso = this.sevProcesoFormService.getSevProceso(this.editForm);
    if (sevProceso.id !== null) {
      this.subscribeToSaveResponse(this.sevProcesoService.update(sevProceso));
    } else {
      this.subscribeToSaveResponse(this.sevProcesoService.create(sevProceso));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ISevProceso>>): void {
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

  protected updateForm(sevProceso: ISevProceso): void {
    this.sevProceso = sevProceso;
    this.sevProcesoFormService.resetForm(this.editForm, sevProceso);

    this.sevUnidadNegociosSharedCollection = this.sevUnidadNegocioService.addSevUnidadNegocioToCollectionIfMissing<ISevUnidadNegocio>(
      this.sevUnidadNegociosSharedCollection,
      sevProceso.unidadNegocio,
    );
  }

  protected loadRelationshipsOptions(): void {
    this.sevUnidadNegocioService
      .query()
      .pipe(map((res: HttpResponse<ISevUnidadNegocio[]>) => res.body ?? []))
      .pipe(
        map((sevUnidadNegocios: ISevUnidadNegocio[]) =>
          this.sevUnidadNegocioService.addSevUnidadNegocioToCollectionIfMissing<ISevUnidadNegocio>(
            sevUnidadNegocios,
            this.sevProceso?.unidadNegocio,
          ),
        ),
      )
      .subscribe((sevUnidadNegocios: ISevUnidadNegocio[]) => (this.sevUnidadNegociosSharedCollection = sevUnidadNegocios));
  }
}
