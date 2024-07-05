import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideHttpClient, HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject, from } from 'rxjs';

import { SevUnidadNegocioService } from '../service/sev-unidad-negocio.service';
import { ISevUnidadNegocio } from '../sev-unidad-negocio.model';
import { SevUnidadNegocioFormService } from './sev-unidad-negocio-form.service';

import { SevUnidadNegocioUpdateComponent } from './sev-unidad-negocio-update.component';

describe('SevUnidadNegocio Management Update Component', () => {
  let comp: SevUnidadNegocioUpdateComponent;
  let fixture: ComponentFixture<SevUnidadNegocioUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let sevUnidadNegocioFormService: SevUnidadNegocioFormService;
  let sevUnidadNegocioService: SevUnidadNegocioService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [SevUnidadNegocioUpdateComponent],
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
      .overrideTemplate(SevUnidadNegocioUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(SevUnidadNegocioUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    sevUnidadNegocioFormService = TestBed.inject(SevUnidadNegocioFormService);
    sevUnidadNegocioService = TestBed.inject(SevUnidadNegocioService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const sevUnidadNegocio: ISevUnidadNegocio = { id: 456 };

      activatedRoute.data = of({ sevUnidadNegocio });
      comp.ngOnInit();

      expect(comp.sevUnidadNegocio).toEqual(sevUnidadNegocio);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ISevUnidadNegocio>>();
      const sevUnidadNegocio = { id: 123 };
      jest.spyOn(sevUnidadNegocioFormService, 'getSevUnidadNegocio').mockReturnValue(sevUnidadNegocio);
      jest.spyOn(sevUnidadNegocioService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ sevUnidadNegocio });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: sevUnidadNegocio }));
      saveSubject.complete();

      // THEN
      expect(sevUnidadNegocioFormService.getSevUnidadNegocio).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(sevUnidadNegocioService.update).toHaveBeenCalledWith(expect.objectContaining(sevUnidadNegocio));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ISevUnidadNegocio>>();
      const sevUnidadNegocio = { id: 123 };
      jest.spyOn(sevUnidadNegocioFormService, 'getSevUnidadNegocio').mockReturnValue({ id: null });
      jest.spyOn(sevUnidadNegocioService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ sevUnidadNegocio: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: sevUnidadNegocio }));
      saveSubject.complete();

      // THEN
      expect(sevUnidadNegocioFormService.getSevUnidadNegocio).toHaveBeenCalled();
      expect(sevUnidadNegocioService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ISevUnidadNegocio>>();
      const sevUnidadNegocio = { id: 123 };
      jest.spyOn(sevUnidadNegocioService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ sevUnidadNegocio });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(sevUnidadNegocioService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
