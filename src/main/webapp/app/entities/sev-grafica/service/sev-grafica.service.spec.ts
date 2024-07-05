import { TestBed } from '@angular/core/testing';
import { provideHttpClientTesting, HttpTestingController } from '@angular/common/http/testing';
import { provideHttpClient } from '@angular/common/http';

import { ISevGrafica } from '../sev-grafica.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../sev-grafica.test-samples';

import { SevGraficaService } from './sev-grafica.service';

const requireRestSample: ISevGrafica = {
  ...sampleWithRequiredData,
};

describe('SevGrafica Service', () => {
  let service: SevGraficaService;
  let httpMock: HttpTestingController;
  let expectedResult: ISevGrafica | ISevGrafica[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(SevGraficaService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.find(123).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should create a SevGrafica', () => {
      const sevGrafica = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(sevGrafica).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a SevGrafica', () => {
      const sevGrafica = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(sevGrafica).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a SevGrafica', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of SevGrafica', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a SevGrafica', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addSevGraficaToCollectionIfMissing', () => {
      it('should add a SevGrafica to an empty array', () => {
        const sevGrafica: ISevGrafica = sampleWithRequiredData;
        expectedResult = service.addSevGraficaToCollectionIfMissing([], sevGrafica);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(sevGrafica);
      });

      it('should not add a SevGrafica to an array that contains it', () => {
        const sevGrafica: ISevGrafica = sampleWithRequiredData;
        const sevGraficaCollection: ISevGrafica[] = [
          {
            ...sevGrafica,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addSevGraficaToCollectionIfMissing(sevGraficaCollection, sevGrafica);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a SevGrafica to an array that doesn't contain it", () => {
        const sevGrafica: ISevGrafica = sampleWithRequiredData;
        const sevGraficaCollection: ISevGrafica[] = [sampleWithPartialData];
        expectedResult = service.addSevGraficaToCollectionIfMissing(sevGraficaCollection, sevGrafica);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(sevGrafica);
      });

      it('should add only unique SevGrafica to an array', () => {
        const sevGraficaArray: ISevGrafica[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const sevGraficaCollection: ISevGrafica[] = [sampleWithRequiredData];
        expectedResult = service.addSevGraficaToCollectionIfMissing(sevGraficaCollection, ...sevGraficaArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const sevGrafica: ISevGrafica = sampleWithRequiredData;
        const sevGrafica2: ISevGrafica = sampleWithPartialData;
        expectedResult = service.addSevGraficaToCollectionIfMissing([], sevGrafica, sevGrafica2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(sevGrafica);
        expect(expectedResult).toContain(sevGrafica2);
      });

      it('should accept null and undefined values', () => {
        const sevGrafica: ISevGrafica = sampleWithRequiredData;
        expectedResult = service.addSevGraficaToCollectionIfMissing([], null, sevGrafica, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(sevGrafica);
      });

      it('should return initial array if no SevGrafica is added', () => {
        const sevGraficaCollection: ISevGrafica[] = [sampleWithRequiredData];
        expectedResult = service.addSevGraficaToCollectionIfMissing(sevGraficaCollection, undefined, null);
        expect(expectedResult).toEqual(sevGraficaCollection);
      });
    });

    describe('compareSevGrafica', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareSevGrafica(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareSevGrafica(entity1, entity2);
        const compareResult2 = service.compareSevGrafica(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareSevGrafica(entity1, entity2);
        const compareResult2 = service.compareSevGrafica(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareSevGrafica(entity1, entity2);
        const compareResult2 = service.compareSevGrafica(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
