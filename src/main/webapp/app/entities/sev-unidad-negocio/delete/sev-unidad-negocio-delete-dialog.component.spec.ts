jest.mock('@ng-bootstrap/ng-bootstrap');

import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { provideHttpClient, HttpResponse } from '@angular/common/http';
import { of } from 'rxjs';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { SevUnidadNegocioService } from '../service/sev-unidad-negocio.service';

import { SevUnidadNegocioDeleteDialogComponent } from './sev-unidad-negocio-delete-dialog.component';

describe('SevUnidadNegocio Management Delete Component', () => {
  let comp: SevUnidadNegocioDeleteDialogComponent;
  let fixture: ComponentFixture<SevUnidadNegocioDeleteDialogComponent>;
  let service: SevUnidadNegocioService;
  let mockActiveModal: NgbActiveModal;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [SevUnidadNegocioDeleteDialogComponent],
      providers: [provideHttpClient(), NgbActiveModal],
    })
      .overrideTemplate(SevUnidadNegocioDeleteDialogComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(SevUnidadNegocioDeleteDialogComponent);
    comp = fixture.componentInstance;
    service = TestBed.inject(SevUnidadNegocioService);
    mockActiveModal = TestBed.inject(NgbActiveModal);
  });

  describe('confirmDelete', () => {
    it('Should call delete service on confirmDelete', inject(
      [],
      fakeAsync(() => {
        // GIVEN
        jest.spyOn(service, 'delete').mockReturnValue(of(new HttpResponse({ body: {} })));

        // WHEN
        comp.confirmDelete(123);
        tick();

        // THEN
        expect(service.delete).toHaveBeenCalledWith(123);
        expect(mockActiveModal.close).toHaveBeenCalledWith('deleted');
      }),
    ));

    it('Should not call delete service on clear', () => {
      // GIVEN
      jest.spyOn(service, 'delete');

      // WHEN
      comp.cancel();

      // THEN
      expect(service.delete).not.toHaveBeenCalled();
      expect(mockActiveModal.close).not.toHaveBeenCalled();
      expect(mockActiveModal.dismiss).toHaveBeenCalled();
    });
  });
});
