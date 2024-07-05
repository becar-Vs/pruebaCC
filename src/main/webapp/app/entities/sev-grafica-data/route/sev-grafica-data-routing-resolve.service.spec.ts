import { TestBed } from '@angular/core/testing';
import { provideHttpClient, HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, ActivatedRoute, Router, convertToParamMap } from '@angular/router';
import { of } from 'rxjs';

import { ISevGraficaData } from '../sev-grafica-data.model';
import { SevGraficaDataService } from '../service/sev-grafica-data.service';

import sevGraficaDataResolve from './sev-grafica-data-routing-resolve.service';

describe('SevGraficaData routing resolve service', () => {
  let mockRouter: Router;
  let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
  let service: SevGraficaDataService;
  let resultSevGraficaData: ISevGraficaData | null | undefined;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [
        provideHttpClient(),
        {
          provide: ActivatedRoute,
          useValue: {
            snapshot: {
              paramMap: convertToParamMap({}),
            },
          },
        },
      ],
    });
    mockRouter = TestBed.inject(Router);
    jest.spyOn(mockRouter, 'navigate').mockImplementation(() => Promise.resolve(true));
    mockActivatedRouteSnapshot = TestBed.inject(ActivatedRoute).snapshot;
    service = TestBed.inject(SevGraficaDataService);
    resultSevGraficaData = undefined;
  });

  describe('resolve', () => {
    it('should return ISevGraficaData returned by find', () => {
      // GIVEN
      service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      TestBed.runInInjectionContext(() => {
        sevGraficaDataResolve(mockActivatedRouteSnapshot).subscribe({
          next(result) {
            resultSevGraficaData = result;
          },
        });
      });

      // THEN
      expect(service.find).toHaveBeenCalledWith(123);
      expect(resultSevGraficaData).toEqual({ id: 123 });
    });

    it('should return null if id is not provided', () => {
      // GIVEN
      service.find = jest.fn();
      mockActivatedRouteSnapshot.params = {};

      // WHEN
      TestBed.runInInjectionContext(() => {
        sevGraficaDataResolve(mockActivatedRouteSnapshot).subscribe({
          next(result) {
            resultSevGraficaData = result;
          },
        });
      });

      // THEN
      expect(service.find).not.toBeCalled();
      expect(resultSevGraficaData).toEqual(null);
    });

    it('should route to 404 page if data not found in server', () => {
      // GIVEN
      jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse<ISevGraficaData>({ body: null })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      TestBed.runInInjectionContext(() => {
        sevGraficaDataResolve(mockActivatedRouteSnapshot).subscribe({
          next(result) {
            resultSevGraficaData = result;
          },
        });
      });

      // THEN
      expect(service.find).toHaveBeenCalledWith(123);
      expect(resultSevGraficaData).toEqual(undefined);
      expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
    });
  });
});
