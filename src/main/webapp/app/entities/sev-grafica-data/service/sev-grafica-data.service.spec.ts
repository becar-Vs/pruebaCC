import { TestBed } from '@angular/core/testing';
import { provideHttpClientTesting, HttpTestingController } from '@angular/common/http/testing';
import { provideHttpClient } from '@angular/common/http';

import { DATE_FORMAT } from 'app/config/input.constants';
import { ISevGraficaData } from '../sev-grafica-data.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../sev-grafica-data.test-samples';

import { SevGraficaDataService, RestSevGraficaData } from './sev-grafica-data.service';

const requireRestSample: RestSevGraficaData = {
  ...sampleWithRequiredData,
  fechaObjetivo: sampleWithRequiredData.fechaObjetivo?.format(DATE_FORMAT),
};

describe('SevGraficaData Service', () => {
  let service: SevGraficaDataService;
  let httpMock: HttpTestingController;
  let expectedResult: ISevGraficaData | ISevGraficaData[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(SevGraficaDataService);
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

    it('should create a SevGraficaData', () => {
      const sevGraficaData = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(sevGraficaData).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a SevGraficaData', () => {
      const sevGraficaData = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(sevGraficaData).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a SevGraficaData', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of SevGraficaData', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a SevGraficaData', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addSevGraficaDataToCollectionIfMissing', () => {
      it('should add a SevGraficaData to an empty array', () => {
        const sevGraficaData: ISevGraficaData = sampleWithRequiredData;
        expectedResult = service.addSevGraficaDataToCollectionIfMissing([], sevGraficaData);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(sevGraficaData);
      });

      it('should not add a SevGraficaData to an array that contains it', () => {
        const sevGraficaData: ISevGraficaData = sampleWithRequiredData;
        const sevGraficaDataCollection: ISevGraficaData[] = [
          {
            ...sevGraficaData,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addSevGraficaDataToCollectionIfMissing(sevGraficaDataCollection, sevGraficaData);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a SevGraficaData to an array that doesn't contain it", () => {
        const sevGraficaData: ISevGraficaData = sampleWithRequiredData;
        const sevGraficaDataCollection: ISevGraficaData[] = [sampleWithPartialData];
        expectedResult = service.addSevGraficaDataToCollectionIfMissing(sevGraficaDataCollection, sevGraficaData);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(sevGraficaData);
      });

      it('should add only unique SevGraficaData to an array', () => {
        const sevGraficaDataArray: ISevGraficaData[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const sevGraficaDataCollection: ISevGraficaData[] = [sampleWithRequiredData];
        expectedResult = service.addSevGraficaDataToCollectionIfMissing(sevGraficaDataCollection, ...sevGraficaDataArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const sevGraficaData: ISevGraficaData = sampleWithRequiredData;
        const sevGraficaData2: ISevGraficaData = sampleWithPartialData;
        expectedResult = service.addSevGraficaDataToCollectionIfMissing([], sevGraficaData, sevGraficaData2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(sevGraficaData);
        expect(expectedResult).toContain(sevGraficaData2);
      });

      it('should accept null and undefined values', () => {
        const sevGraficaData: ISevGraficaData = sampleWithRequiredData;
        expectedResult = service.addSevGraficaDataToCollectionIfMissing([], null, sevGraficaData, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(sevGraficaData);
      });

      it('should return initial array if no SevGraficaData is added', () => {
        const sevGraficaDataCollection: ISevGraficaData[] = [sampleWithRequiredData];
        expectedResult = service.addSevGraficaDataToCollectionIfMissing(sevGraficaDataCollection, undefined, null);
        expect(expectedResult).toEqual(sevGraficaDataCollection);
      });
    });

    describe('compareSevGraficaData', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareSevGraficaData(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareSevGraficaData(entity1, entity2);
        const compareResult2 = service.compareSevGraficaData(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareSevGraficaData(entity1, entity2);
        const compareResult2 = service.compareSevGraficaData(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareSevGraficaData(entity1, entity2);
        const compareResult2 = service.compareSevGraficaData(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
