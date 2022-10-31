import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IEquipo } from '../equipo.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../equipo.test-samples';

import { EquipoService } from './equipo.service';

const requireRestSample: IEquipo = {
  ...sampleWithRequiredData,
};

describe('Equipo Service', () => {
  let service: EquipoService;
  let httpMock: HttpTestingController;
  let expectedResult: IEquipo | IEquipo[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(EquipoService);
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

    it('should create a Equipo', () => {
      // eslint-disable-next-line @typescript-eslint/no-unused-vars
      const equipo = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(equipo).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Equipo', () => {
      const equipo = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(equipo).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Equipo', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Equipo', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a Equipo', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addEquipoToCollectionIfMissing', () => {
      it('should add a Equipo to an empty array', () => {
        const equipo: IEquipo = sampleWithRequiredData;
        expectedResult = service.addEquipoToCollectionIfMissing([], equipo);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(equipo);
      });

      it('should not add a Equipo to an array that contains it', () => {
        const equipo: IEquipo = sampleWithRequiredData;
        const equipoCollection: IEquipo[] = [
          {
            ...equipo,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addEquipoToCollectionIfMissing(equipoCollection, equipo);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Equipo to an array that doesn't contain it", () => {
        const equipo: IEquipo = sampleWithRequiredData;
        const equipoCollection: IEquipo[] = [sampleWithPartialData];
        expectedResult = service.addEquipoToCollectionIfMissing(equipoCollection, equipo);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(equipo);
      });

      it('should add only unique Equipo to an array', () => {
        const equipoArray: IEquipo[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const equipoCollection: IEquipo[] = [sampleWithRequiredData];
        expectedResult = service.addEquipoToCollectionIfMissing(equipoCollection, ...equipoArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const equipo: IEquipo = sampleWithRequiredData;
        const equipo2: IEquipo = sampleWithPartialData;
        expectedResult = service.addEquipoToCollectionIfMissing([], equipo, equipo2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(equipo);
        expect(expectedResult).toContain(equipo2);
      });

      it('should accept null and undefined values', () => {
        const equipo: IEquipo = sampleWithRequiredData;
        expectedResult = service.addEquipoToCollectionIfMissing([], null, equipo, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(equipo);
      });

      it('should return initial array if no Equipo is added', () => {
        const equipoCollection: IEquipo[] = [sampleWithRequiredData];
        expectedResult = service.addEquipoToCollectionIfMissing(equipoCollection, undefined, null);
        expect(expectedResult).toEqual(equipoCollection);
      });
    });

    describe('compareEquipo', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareEquipo(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareEquipo(entity1, entity2);
        const compareResult2 = service.compareEquipo(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareEquipo(entity1, entity2);
        const compareResult2 = service.compareEquipo(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareEquipo(entity1, entity2);
        const compareResult2 = service.compareEquipo(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
