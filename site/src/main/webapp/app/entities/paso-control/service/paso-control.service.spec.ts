import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IPasoControl } from '../paso-control.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../paso-control.test-samples';

import { PasoControlService, RestPasoControl } from './paso-control.service';

const requireRestSample: RestPasoControl = {
  ...sampleWithRequiredData,
  fechaHora: sampleWithRequiredData.fechaHora?.toJSON(),
};

describe('PasoControl Service', () => {
  let service: PasoControlService;
  let httpMock: HttpTestingController;
  let expectedResult: IPasoControl | IPasoControl[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(PasoControlService);
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

    it('should create a PasoControl', () => {
      // eslint-disable-next-line @typescript-eslint/no-unused-vars
      const pasoControl = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(pasoControl).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a PasoControl', () => {
      const pasoControl = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(pasoControl).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a PasoControl', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of PasoControl', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a PasoControl', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addPasoControlToCollectionIfMissing', () => {
      it('should add a PasoControl to an empty array', () => {
        const pasoControl: IPasoControl = sampleWithRequiredData;
        expectedResult = service.addPasoControlToCollectionIfMissing([], pasoControl);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(pasoControl);
      });

      it('should not add a PasoControl to an array that contains it', () => {
        const pasoControl: IPasoControl = sampleWithRequiredData;
        const pasoControlCollection: IPasoControl[] = [
          {
            ...pasoControl,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addPasoControlToCollectionIfMissing(pasoControlCollection, pasoControl);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a PasoControl to an array that doesn't contain it", () => {
        const pasoControl: IPasoControl = sampleWithRequiredData;
        const pasoControlCollection: IPasoControl[] = [sampleWithPartialData];
        expectedResult = service.addPasoControlToCollectionIfMissing(pasoControlCollection, pasoControl);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(pasoControl);
      });

      it('should add only unique PasoControl to an array', () => {
        const pasoControlArray: IPasoControl[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const pasoControlCollection: IPasoControl[] = [sampleWithRequiredData];
        expectedResult = service.addPasoControlToCollectionIfMissing(pasoControlCollection, ...pasoControlArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const pasoControl: IPasoControl = sampleWithRequiredData;
        const pasoControl2: IPasoControl = sampleWithPartialData;
        expectedResult = service.addPasoControlToCollectionIfMissing([], pasoControl, pasoControl2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(pasoControl);
        expect(expectedResult).toContain(pasoControl2);
      });

      it('should accept null and undefined values', () => {
        const pasoControl: IPasoControl = sampleWithRequiredData;
        expectedResult = service.addPasoControlToCollectionIfMissing([], null, pasoControl, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(pasoControl);
      });

      it('should return initial array if no PasoControl is added', () => {
        const pasoControlCollection: IPasoControl[] = [sampleWithRequiredData];
        expectedResult = service.addPasoControlToCollectionIfMissing(pasoControlCollection, undefined, null);
        expect(expectedResult).toEqual(pasoControlCollection);
      });
    });

    describe('comparePasoControl', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.comparePasoControl(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.comparePasoControl(entity1, entity2);
        const compareResult2 = service.comparePasoControl(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.comparePasoControl(entity1, entity2);
        const compareResult2 = service.comparePasoControl(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.comparePasoControl(entity1, entity2);
        const compareResult2 = service.comparePasoControl(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
