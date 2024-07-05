import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideHttpClient, HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject, from } from 'rxjs';

import { ISevUnidadNegocio } from 'app/entities/sev-unidad-negocio/sev-unidad-negocio.model';
import { SevUnidadNegocioService } from 'app/entities/sev-unidad-negocio/service/sev-unidad-negocio.service';
import { SevProcesoService } from '../service/sev-proceso.service';
import { ISevProceso } from '../sev-proceso.model';
import { SevProcesoFormService } from './sev-proceso-form.service';

import { SevProcesoUpdateComponent } from './sev-proceso-update.component';

describe('SevProceso Management Update Component', () => {
  let comp: SevProcesoUpdateComponent;
  let fixture: ComponentFixture<SevProcesoUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let sevProcesoFormService: SevProcesoFormService;
  let sevProcesoService: SevProcesoService;
  let sevUnidadNegocioService: SevUnidadNegocioService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [SevProcesoUpdateComponent],
      providers: [
        provideHttpClient(),
        FormBuilder,
        {
          provide: ActivatedRoute,
          useValue: {
            params: from([{}]),
          },
        },
      ],
    })
      .overrideTemplate(SevProcesoUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(SevProcesoUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    sevProcesoFormService = TestBed.inject(SevProcesoFormService);
    sevProcesoService = TestBed.inject(SevProcesoService);
    sevUnidadNegocioService = TestBed.inject(SevUnidadNegocioService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call SevUnidadNegocio query and add missing value', () => {
      const sevProceso: ISevProceso = { id: 456 };
      const unidadNegocio: ISevUnidadNegocio = { id: 13113 };
      sevProceso.unidadNegocio = unidadNegocio;

      const sevUnidadNegocioCollection: ISevUnidadNegocio[] = [{ id: 2349 }];
      jest.spyOn(sevUnidadNegocioService, 'query').mockReturnValue(of(new HttpResponse({ body: sevUnidadNegocioCollection })));
      const additionalSevUnidadNegocios = [unidadNegocio];
      const expectedCollection: ISevUnidadNegocio[] = [...additionalSevUnidadNegocios, ...sevUnidadNegocioCollection];
      jest.spyOn(sevUnidadNegocioService, 'addSevUnidadNegocioToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ sevProceso });
      comp.ngOnInit();

      expect(sevUnidadNegocioService.query).toHaveBeenCalled();
      expect(sevUnidadNegocioService.addSevUnidadNegocioToCollectionIfMissing).toHaveBeenCalledWith(
        sevUnidadNegocioCollection,
        ...additionalSevUnidadNegocios.map(expect.objectContaining),
      );
      expect(comp.sevUnidadNegociosSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const sevProceso: ISevProceso = { id: 456 };
      const unidadNegocio: ISevUnidadNegocio = { id: 6879 };
      sevProceso.unidadNegocio = unidadNegocio;

      activatedRoute.data = of({ sevProceso });
      comp.ngOnInit();

      expect(comp.sevUnidadNegociosSharedCollection).toContain(unidadNegocio);
      expect(comp.sevProceso).toEqual(sevProceso);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ISevProceso>>();
      const sevProceso = { id: 123 };
      jest.spyOn(sevProcesoFormService, 'getSevProceso').mockReturnValue(sevProceso);
      jest.spyOn(sevProcesoService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ sevProceso });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: sevProceso }));
      saveSubject.complete();

      // THEN
      expect(sevProcesoFormService.getSevProceso).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(sevProcesoService.update).toHaveBeenCalledWith(expect.objectContaining(sevProceso));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ISevProceso>>();
      const sevProceso = { id: 123 };
      jest.spyOn(sevProcesoFormService, 'getSevProceso').mockReturnValue({ id: null });
      jest.spyOn(sevProcesoService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ sevProceso: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: sevProceso }));
      saveSubject.complete();

      // THEN
      expect(sevProcesoFormService.getSevProceso).toHaveBeenCalled();
      expect(sevProcesoService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ISevProceso>>();
      const sevProceso = { id: 123 };
      jest.spyOn(sevProcesoService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ sevProceso });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(sevProcesoService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareSevUnidadNegocio', () => {
      it('Should forward to sevUnidadNegocioService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(sevUnidadNegocioService, 'compareSevUnidadNegocio');
        comp.compareSevUnidadNegocio(entity, entity2);
        expect(sevUnidadNegocioService.compareSevUnidadNegocio).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
