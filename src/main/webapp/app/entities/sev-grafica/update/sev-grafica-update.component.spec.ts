import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideHttpClient, HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject, from } from 'rxjs';

import { ISevProceso } from 'app/entities/sev-proceso/sev-proceso.model';
import { SevProcesoService } from 'app/entities/sev-proceso/service/sev-proceso.service';
import { SevGraficaService } from '../service/sev-grafica.service';
import { ISevGrafica } from '../sev-grafica.model';
import { SevGraficaFormService } from './sev-grafica-form.service';

import { SevGraficaUpdateComponent } from './sev-grafica-update.component';

describe('SevGrafica Management Update Component', () => {
  let comp: SevGraficaUpdateComponent;
  let fixture: ComponentFixture<SevGraficaUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let sevGraficaFormService: SevGraficaFormService;
  let sevGraficaService: SevGraficaService;
  let sevProcesoService: SevProcesoService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [SevGraficaUpdateComponent],
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
      .overrideTemplate(SevGraficaUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(SevGraficaUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    sevGraficaFormService = TestBed.inject(SevGraficaFormService);
    sevGraficaService = TestBed.inject(SevGraficaService);
    sevProcesoService = TestBed.inject(SevProcesoService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call SevProceso query and add missing value', () => {
      const sevGrafica: ISevGrafica = { id: 456 };
      const proceso: ISevProceso = { id: 20529 };
      sevGrafica.proceso = proceso;

      const sevProcesoCollection: ISevProceso[] = [{ id: 19262 }];
      jest.spyOn(sevProcesoService, 'query').mockReturnValue(of(new HttpResponse({ body: sevProcesoCollection })));
      const additionalSevProcesos = [proceso];
      const expectedCollection: ISevProceso[] = [...additionalSevProcesos, ...sevProcesoCollection];
      jest.spyOn(sevProcesoService, 'addSevProcesoToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ sevGrafica });
      comp.ngOnInit();

      expect(sevProcesoService.query).toHaveBeenCalled();
      expect(sevProcesoService.addSevProcesoToCollectionIfMissing).toHaveBeenCalledWith(
        sevProcesoCollection,
        ...additionalSevProcesos.map(expect.objectContaining),
      );
      expect(comp.sevProcesosSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const sevGrafica: ISevGrafica = { id: 456 };
      const proceso: ISevProceso = { id: 29290 };
      sevGrafica.proceso = proceso;

      activatedRoute.data = of({ sevGrafica });
      comp.ngOnInit();

      expect(comp.sevProcesosSharedCollection).toContain(proceso);
      expect(comp.sevGrafica).toEqual(sevGrafica);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ISevGrafica>>();
      const sevGrafica = { id: 123 };
      jest.spyOn(sevGraficaFormService, 'getSevGrafica').mockReturnValue(sevGrafica);
      jest.spyOn(sevGraficaService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ sevGrafica });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: sevGrafica }));
      saveSubject.complete();

      // THEN
      expect(sevGraficaFormService.getSevGrafica).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(sevGraficaService.update).toHaveBeenCalledWith(expect.objectContaining(sevGrafica));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ISevGrafica>>();
      const sevGrafica = { id: 123 };
      jest.spyOn(sevGraficaFormService, 'getSevGrafica').mockReturnValue({ id: null });
      jest.spyOn(sevGraficaService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ sevGrafica: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: sevGrafica }));
      saveSubject.complete();

      // THEN
      expect(sevGraficaFormService.getSevGrafica).toHaveBeenCalled();
      expect(sevGraficaService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ISevGrafica>>();
      const sevGrafica = { id: 123 };
      jest.spyOn(sevGraficaService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ sevGrafica });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(sevGraficaService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareSevProceso', () => {
      it('Should forward to sevProcesoService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(sevProcesoService, 'compareSevProceso');
        comp.compareSevProceso(entity, entity2);
        expect(sevProcesoService.compareSevProceso).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
