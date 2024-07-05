import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideHttpClient, HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject, from } from 'rxjs';

import { ISevGrafica } from 'app/entities/sev-grafica/sev-grafica.model';
import { SevGraficaService } from 'app/entities/sev-grafica/service/sev-grafica.service';
import { SevGraficaDataService } from '../service/sev-grafica-data.service';
import { ISevGraficaData } from '../sev-grafica-data.model';
import { SevGraficaDataFormService } from './sev-grafica-data-form.service';

import { SevGraficaDataUpdateComponent } from './sev-grafica-data-update.component';

describe('SevGraficaData Management Update Component', () => {
  let comp: SevGraficaDataUpdateComponent;
  let fixture: ComponentFixture<SevGraficaDataUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let sevGraficaDataFormService: SevGraficaDataFormService;
  let sevGraficaDataService: SevGraficaDataService;
  let sevGraficaService: SevGraficaService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [SevGraficaDataUpdateComponent],
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
      .overrideTemplate(SevGraficaDataUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(SevGraficaDataUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    sevGraficaDataFormService = TestBed.inject(SevGraficaDataFormService);
    sevGraficaDataService = TestBed.inject(SevGraficaDataService);
    sevGraficaService = TestBed.inject(SevGraficaService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call SevGrafica query and add missing value', () => {
      const sevGraficaData: ISevGraficaData = { id: 456 };
      const grafica: ISevGrafica = { id: 17907 };
      sevGraficaData.grafica = grafica;

      const sevGraficaCollection: ISevGrafica[] = [{ id: 2640 }];
      jest.spyOn(sevGraficaService, 'query').mockReturnValue(of(new HttpResponse({ body: sevGraficaCollection })));
      const additionalSevGraficas = [grafica];
      const expectedCollection: ISevGrafica[] = [...additionalSevGraficas, ...sevGraficaCollection];
      jest.spyOn(sevGraficaService, 'addSevGraficaToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ sevGraficaData });
      comp.ngOnInit();

      expect(sevGraficaService.query).toHaveBeenCalled();
      expect(sevGraficaService.addSevGraficaToCollectionIfMissing).toHaveBeenCalledWith(
        sevGraficaCollection,
        ...additionalSevGraficas.map(expect.objectContaining),
      );
      expect(comp.sevGraficasSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const sevGraficaData: ISevGraficaData = { id: 456 };
      const grafica: ISevGrafica = { id: 4660 };
      sevGraficaData.grafica = grafica;

      activatedRoute.data = of({ sevGraficaData });
      comp.ngOnInit();

      expect(comp.sevGraficasSharedCollection).toContain(grafica);
      expect(comp.sevGraficaData).toEqual(sevGraficaData);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ISevGraficaData>>();
      const sevGraficaData = { id: 123 };
      jest.spyOn(sevGraficaDataFormService, 'getSevGraficaData').mockReturnValue(sevGraficaData);
      jest.spyOn(sevGraficaDataService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ sevGraficaData });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: sevGraficaData }));
      saveSubject.complete();

      // THEN
      expect(sevGraficaDataFormService.getSevGraficaData).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(sevGraficaDataService.update).toHaveBeenCalledWith(expect.objectContaining(sevGraficaData));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ISevGraficaData>>();
      const sevGraficaData = { id: 123 };
      jest.spyOn(sevGraficaDataFormService, 'getSevGraficaData').mockReturnValue({ id: null });
      jest.spyOn(sevGraficaDataService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ sevGraficaData: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: sevGraficaData }));
      saveSubject.complete();

      // THEN
      expect(sevGraficaDataFormService.getSevGraficaData).toHaveBeenCalled();
      expect(sevGraficaDataService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ISevGraficaData>>();
      const sevGraficaData = { id: 123 };
      jest.spyOn(sevGraficaDataService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ sevGraficaData });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(sevGraficaDataService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareSevGrafica', () => {
      it('Should forward to sevGraficaService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(sevGraficaService, 'compareSevGrafica');
        comp.compareSevGrafica(entity, entity2);
        expect(sevGraficaService.compareSevGrafica).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
