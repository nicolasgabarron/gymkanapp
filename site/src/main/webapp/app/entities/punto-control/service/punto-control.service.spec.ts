import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IPuntoControl } from '../punto-control.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../punto-control.test-samples';

import { PuntoControlService } from './punto-control.service';

const requireRestSample: IPuntoControl = {
  ...sampleWithRequiredData,
};

describe('PuntoControl Service', () => {
  let service: PuntoControlService;
  let httpMock: HttpTestingController;
  let expectedResult: IPuntoControl | IPuntoControl[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(PuntoControlService);
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

    it('should create a PuntoControl', () => {
      // eslint-disable-next-line @typescript-eslint/no-unused-vars
      const puntoControl = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(puntoControl).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a PuntoControl', () => {
      const puntoControl = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(puntoControl).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a PuntoControl', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of PuntoControl', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a PuntoControl', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addPuntoControlToCollectionIfMissing', () => {
      it('should add a PuntoControl to an empty array', () => {
        const puntoControl: IPuntoControl = sampleWithRequiredData;
        expectedResult = service.addPuntoControlToCollectionIfMissing([], puntoControl);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(puntoControl);
      });

      it('should not add a PuntoControl to an array that contains it', () => {
        const puntoControl: IPuntoControl = sampleWithRequiredData;
        const puntoControlCollection: IPuntoControl[] = [
          {
            ...puntoControl,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addPuntoControlToCollectionIfMissing(puntoControlCollection, puntoControl);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a PuntoControl to an array that doesn't contain it", () => {
        const puntoControl: IPuntoControl = sampleWithRequiredData;
        const puntoControlCollection: IPuntoControl[] = [sampleWithPartialData];
        expectedResult = service.addPuntoControlToCollectionIfMissing(puntoControlCollection, puntoControl);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(puntoControl);
      });

      it('should add only unique PuntoControl to an array', () => {
        const puntoControlArray: IPuntoControl[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const puntoControlCollection: IPuntoControl[] = [sampleWithRequiredData];
        expectedResult = service.addPuntoControlToCollectionIfMissing(puntoControlCollection, ...puntoControlArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const puntoControl: IPuntoControl = sampleWithRequiredData;
        const puntoControl2: IPuntoControl = sampleWithPartialData;
        expectedResult = service.addPuntoControlToCollectionIfMissing([], puntoControl, puntoControl2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(puntoControl);
        expect(expectedResult).toContain(puntoControl2);
      });

      it('should accept null and undefined values', () => {
        const puntoControl: IPuntoControl = sampleWithRequiredData;
        expectedResult = service.addPuntoControlToCollectionIfMissing([], null, puntoControl, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(puntoControl);
      });

      it('should return initial array if no PuntoControl is added', () => {
        const puntoControlCollection: IPuntoControl[] = [sampleWithRequiredData];
        expectedResult = service.addPuntoControlToCollectionIfMissing(puntoControlCollection, undefined, null);
        expect(expectedResult).toEqual(puntoControlCollection);
      });
    });

    describe('comparePuntoControl', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.comparePuntoControl(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.comparePuntoControl(entity1, entity2);
        const compareResult2 = service.comparePuntoControl(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.comparePuntoControl(entity1, entity2);
        const compareResult2 = service.comparePuntoControl(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.comparePuntoControl(entity1, entity2);
        const compareResult2 = service.comparePuntoControl(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
