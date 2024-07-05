import { Component, inject, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { ISevGrafica } from 'app/entities/sev-grafica/sev-grafica.model';
import { SevGraficaService } from 'app/entities/sev-grafica/service/sev-grafica.service';
import { ISevGraficaData } from '../sev-grafica-data.model';
import { SevGraficaDataService } from '../service/sev-grafica-data.service';
import { SevGraficaDataFormService, SevGraficaDataFormGroup } from './sev-grafica-data-form.service';

@Component({
  standalone: true,
  selector: 'jhi-sev-grafica-data-update',
  templateUrl: './sev-grafica-data-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class SevGraficaDataUpdateComponent implements OnInit {
  isSaving = false;
  sevGraficaData: ISevGraficaData | null = null;

  sevGraficasSharedCollection: ISevGrafica[] = [];

  protected sevGraficaDataService = inject(SevGraficaDataService);
  protected sevGraficaDataFormService = inject(SevGraficaDataFormService);
  protected sevGraficaService = inject(SevGraficaService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: SevGraficaDataFormGroup = this.sevGraficaDataFormService.createSevGraficaDataFormGroup();

  compareSevGrafica = (o1: ISevGrafica | null, o2: ISevGrafica | null): boolean => this.sevGraficaService.compareSevGrafica(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ sevGraficaData }) => {
      this.sevGraficaData = sevGraficaData;
      if (sevGraficaData) {
        this.updateForm(sevGraficaData);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const sevGraficaData = this.sevGraficaDataFormService.getSevGraficaData(this.editForm);
    if (sevGraficaData.id !== null) {
      this.subscribeToSaveResponse(this.sevGraficaDataService.update(sevGraficaData));
    } else {
      this.subscribeToSaveResponse(this.sevGraficaDataService.create(sevGraficaData));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ISevGraficaData>>): void {
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

  protected updateForm(sevGraficaData: ISevGraficaData): void {
    this.sevGraficaData = sevGraficaData;
    this.sevGraficaDataFormService.resetForm(this.editForm, sevGraficaData);

    this.sevGraficasSharedCollection = this.sevGraficaService.addSevGraficaToCollectionIfMissing<ISevGrafica>(
      this.sevGraficasSharedCollection,
      sevGraficaData.grafica,
    );
  }

  protected loadRelationshipsOptions(): void {
    this.sevGraficaService
      .query()
      .pipe(map((res: HttpResponse<ISevGrafica[]>) => res.body ?? []))
      .pipe(
        map((sevGraficas: ISevGrafica[]) =>
          this.sevGraficaService.addSevGraficaToCollectionIfMissing<ISevGrafica>(sevGraficas, this.sevGraficaData?.grafica),
        ),
      )
      .subscribe((sevGraficas: ISevGrafica[]) => (this.sevGraficasSharedCollection = sevGraficas));
  }
}
