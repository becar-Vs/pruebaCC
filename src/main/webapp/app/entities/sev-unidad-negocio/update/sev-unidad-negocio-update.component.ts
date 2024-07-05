import { Component, inject, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { ISevUnidadNegocio } from '../sev-unidad-negocio.model';
import { SevUnidadNegocioService } from '../service/sev-unidad-negocio.service';
import { SevUnidadNegocioFormService, SevUnidadNegocioFormGroup } from './sev-unidad-negocio-form.service';

@Component({
  standalone: true,
  selector: 'jhi-sev-unidad-negocio-update',
  templateUrl: './sev-unidad-negocio-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class SevUnidadNegocioUpdateComponent implements OnInit {
  isSaving = false;
  sevUnidadNegocio: ISevUnidadNegocio | null = null;

  protected sevUnidadNegocioService = inject(SevUnidadNegocioService);
  protected sevUnidadNegocioFormService = inject(SevUnidadNegocioFormService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: SevUnidadNegocioFormGroup = this.sevUnidadNegocioFormService.createSevUnidadNegocioFormGroup();

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ sevUnidadNegocio }) => {
      this.sevUnidadNegocio = sevUnidadNegocio;
      if (sevUnidadNegocio) {
        this.updateForm(sevUnidadNegocio);
      }
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const sevUnidadNegocio = this.sevUnidadNegocioFormService.getSevUnidadNegocio(this.editForm);
    if (sevUnidadNegocio.id !== null) {
      this.subscribeToSaveResponse(this.sevUnidadNegocioService.update(sevUnidadNegocio));
    } else {
      this.subscribeToSaveResponse(this.sevUnidadNegocioService.create(sevUnidadNegocio));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ISevUnidadNegocio>>): void {
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

  protected updateForm(sevUnidadNegocio: ISevUnidadNegocio): void {
    this.sevUnidadNegocio = sevUnidadNegocio;
    this.sevUnidadNegocioFormService.resetForm(this.editForm, sevUnidadNegocio);
  }
}
