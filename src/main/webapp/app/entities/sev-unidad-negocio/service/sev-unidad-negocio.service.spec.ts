import { TestBed } from '@angular/core/testing';
import { provideHttpClientTesting, HttpTestingController } from '@angular/common/http/testing';
import { provideHttpClient } from '@angular/common/http';

import { ISevUnidadNegocio } from '../sev-unidad-negocio.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../sev-unidad-negocio.test-samples';

import { SevUnidadNegocioService } from './sev-unidad-negocio.service';

const requireRestSample: ISevUnidadNegocio = {
  ...sampleWithRequiredData,
};

describe('SevUnidadNegocio Service', () => {
  let service: SevUnidadNegocioService;
  let httpMock: HttpTestingController;
  let expectedResult: ISevUnidadNegocio | ISevUnidadNegocio[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(SevUnidadNegocioService);
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

    it('should create a SevUnidadNegocio', () => {
      const sevUnidadNegocio = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(sevUnidadNegocio).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a SevUnidadNegocio', () => {
      const sevUnidadNegocio = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(sevUnidadNegocio).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a SevUnidadNegocio', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of SevUnidadNegocio', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a SevUnidadNegocio', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addSevUnidadNegocioToCollectionIfMissing', () => {
      it('should add a SevUnidadNegocio to an empty array', () => {
        const sevUnidadNegocio: ISevUnidadNegocio = sampleWithRequiredData;
        expectedResult = service.addSevUnidadNegocioToCollectionIfMissing([], sevUnidadNegocio);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(sevUnidadNegocio);
      });

      it('should not add a SevUnidadNegocio to an array that contains it', () => {
        const sevUnidadNegocio: ISevUnidadNegocio = sampleWithRequiredData;
        const sevUnidadNegocioCollection: ISevUnidadNegocio[] = [
          {
            ...sevUnidadNegocio,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addSevUnidadNegocioToCollectionIfMissing(sevUnidadNegocioCollection, sevUnidadNegocio);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a SevUnidadNegocio to an array that doesn't contain it", () => {
        const sevUnidadNegocio: ISevUnidadNegocio = sampleWithRequiredData;
        const sevUnidadNegocioCollection: ISevUnidadNegocio[] = [sampleWithPartialData];
        expectedResult = service.addSevUnidadNegocioToCollectionIfMissing(sevUnidadNegocioCollection, sevUnidadNegocio);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(sevUnidadNegocio);
      });

      it('should add only unique SevUnidadNegocio to an array', () => {
        const sevUnidadNegocioArray: ISevUnidadNegocio[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const sevUnidadNegocioCollection: ISevUnidadNegocio[] = [sampleWithRequiredData];
        expectedResult = service.addSevUnidadNegocioToCollectionIfMissing(sevUnidadNegocioCollection, ...sevUnidadNegocioArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const sevUnidadNegocio: ISevUnidadNegocio = sampleWithRequiredData;
        const sevUnidadNegocio2: ISevUnidadNegocio = sampleWithPartialData;
        expectedResult = service.addSevUnidadNegocioToCollectionIfMissing([], sevUnidadNegocio, sevUnidadNegocio2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(sevUnidadNegocio);
        expect(expectedResult).toContain(sevUnidadNegocio2);
      });

      it('should accept null and undefined values', () => {
        const sevUnidadNegocio: ISevUnidadNegocio = sampleWithRequiredData;
        expectedResult = service.addSevUnidadNegocioToCollectionIfMissing([], null, sevUnidadNegocio, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(sevUnidadNegocio);
      });

      it('should return initial array if no SevUnidadNegocio is added', () => {
        const sevUnidadNegocioCollection: ISevUnidadNegocio[] = [sampleWithRequiredData];
        expectedResult = service.addSevUnidadNegocioToCollectionIfMissing(sevUnidadNegocioCollection, undefined, null);
        expect(expectedResult).toEqual(sevUnidadNegocioCollection);
      });
    });

    describe('compareSevUnidadNegocio', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareSevUnidadNegocio(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareSevUnidadNegocio(entity1, entity2);
        const compareResult2 = service.compareSevUnidadNegocio(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareSevUnidadNegocio(entity1, entity2);
        const compareResult2 = service.compareSevUnidadNegocio(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareSevUnidadNegocio(entity1, entity2);
        const compareResult2 = service.compareSevUnidadNegocio(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
