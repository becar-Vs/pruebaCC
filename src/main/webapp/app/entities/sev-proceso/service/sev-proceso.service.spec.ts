import { TestBed } from '@angular/core/testing';
import { provideHttpClientTesting, HttpTestingController } from '@angular/common/http/testing';
import { provideHttpClient } from '@angular/common/http';

import { ISevProceso } from '../sev-proceso.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../sev-proceso.test-samples';

import { SevProcesoService } from './sev-proceso.service';

const requireRestSample: ISevProceso = {
  ...sampleWithRequiredData,
};

describe('SevProceso Service', () => {
  let service: SevProcesoService;
  let httpMock: HttpTestingController;
  let expectedResult: ISevProceso | ISevProceso[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(SevProcesoService);
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

    it('should create a SevProceso', () => {
      const sevProceso = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(sevProceso).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a SevProceso', () => {
      const sevProceso = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(sevProceso).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a SevProceso', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of SevProceso', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a SevProceso', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addSevProcesoToCollectionIfMissing', () => {
      it('should add a SevProceso to an empty array', () => {
        const sevProceso: ISevProceso = sampleWithRequiredData;
        expectedResult = service.addSevProcesoToCollectionIfMissing([], sevProceso);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(sevProceso);
      });

      it('should not add a SevProceso to an array that contains it', () => {
        const sevProceso: ISevProceso = sampleWithRequiredData;
        const sevProcesoCollection: ISevProceso[] = [
          {
            ...sevProceso,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addSevProcesoToCollectionIfMissing(sevProcesoCollection, sevProceso);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a SevProceso to an array that doesn't contain it", () => {
        const sevProceso: ISevProceso = sampleWithRequiredData;
        const sevProcesoCollection: ISevProceso[] = [sampleWithPartialData];
        expectedResult = service.addSevProcesoToCollectionIfMissing(sevProcesoCollection, sevProceso);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(sevProceso);
      });

      it('should add only unique SevProceso to an array', () => {
        const sevProcesoArray: ISevProceso[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const sevProcesoCollection: ISevProceso[] = [sampleWithRequiredData];
        expectedResult = service.addSevProcesoToCollectionIfMissing(sevProcesoCollection, ...sevProcesoArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const sevProceso: ISevProceso = sampleWithRequiredData;
        const sevProceso2: ISevProceso = sampleWithPartialData;
        expectedResult = service.addSevProcesoToCollectionIfMissing([], sevProceso, sevProceso2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(sevProceso);
        expect(expectedResult).toContain(sevProceso2);
      });

      it('should accept null and undefined values', () => {
        const sevProceso: ISevProceso = sampleWithRequiredData;
        expectedResult = service.addSevProcesoToCollectionIfMissing([], null, sevProceso, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(sevProceso);
      });

      it('should return initial array if no SevProceso is added', () => {
        const sevProcesoCollection: ISevProceso[] = [sampleWithRequiredData];
        expectedResult = service.addSevProcesoToCollectionIfMissing(sevProcesoCollection, undefined, null);
        expect(expectedResult).toEqual(sevProcesoCollection);
      });
    });

    describe('compareSevProceso', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareSevProceso(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareSevProceso(entity1, entity2);
        const compareResult2 = service.compareSevProceso(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareSevProceso(entity1, entity2);
        const compareResult2 = service.compareSevProceso(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareSevProceso(entity1, entity2);
        const compareResult2 = service.compareSevProceso(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
