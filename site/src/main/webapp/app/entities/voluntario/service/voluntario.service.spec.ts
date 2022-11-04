import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { DATE_FORMAT } from 'app/config/input.constants';
import { IVoluntario } from '../voluntario.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../voluntario.test-samples';

import { VoluntarioService, RestVoluntario } from './voluntario.service';

const requireRestSample: RestVoluntario = {
  ...sampleWithRequiredData,
  fechaNacimiento: sampleWithRequiredData.fechaNacimiento?.format(DATE_FORMAT),
};

describe('Voluntario Service', () => {
  let service: VoluntarioService;
  let httpMock: HttpTestingController;
  let expectedResult: IVoluntario | IVoluntario[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(VoluntarioService);
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

    it('should create a Voluntario', () => {
      // eslint-disable-next-line @typescript-eslint/no-unused-vars
      const voluntario = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(voluntario).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Voluntario', () => {
      const voluntario = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(voluntario).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Voluntario', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Voluntario', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a Voluntario', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addVoluntarioToCollectionIfMissing', () => {
      it('should add a Voluntario to an empty array', () => {
        const voluntario: IVoluntario = sampleWithRequiredData;
        expectedResult = service.addVoluntarioToCollectionIfMissing([], voluntario);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(voluntario);
      });

      it('should not add a Voluntario to an array that contains it', () => {
        const voluntario: IVoluntario = sampleWithRequiredData;
        const voluntarioCollection: IVoluntario[] = [
          {
            ...voluntario,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addVoluntarioToCollectionIfMissing(voluntarioCollection, voluntario);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Voluntario to an array that doesn't contain it", () => {
        const voluntario: IVoluntario = sampleWithRequiredData;
        const voluntarioCollection: IVoluntario[] = [sampleWithPartialData];
        expectedResult = service.addVoluntarioToCollectionIfMissing(voluntarioCollection, voluntario);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(voluntario);
      });

      it('should add only unique Voluntario to an array', () => {
        const voluntarioArray: IVoluntario[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const voluntarioCollection: IVoluntario[] = [sampleWithRequiredData];
        expectedResult = service.addVoluntarioToCollectionIfMissing(voluntarioCollection, ...voluntarioArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const voluntario: IVoluntario = sampleWithRequiredData;
        const voluntario2: IVoluntario = sampleWithPartialData;
        expectedResult = service.addVoluntarioToCollectionIfMissing([], voluntario, voluntario2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(voluntario);
        expect(expectedResult).toContain(voluntario2);
      });

      it('should accept null and undefined values', () => {
        const voluntario: IVoluntario = sampleWithRequiredData;
        expectedResult = service.addVoluntarioToCollectionIfMissing([], null, voluntario, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(voluntario);
      });

      it('should return initial array if no Voluntario is added', () => {
        const voluntarioCollection: IVoluntario[] = [sampleWithRequiredData];
        expectedResult = service.addVoluntarioToCollectionIfMissing(voluntarioCollection, undefined, null);
        expect(expectedResult).toEqual(voluntarioCollection);
      });
    });

    describe('compareVoluntario', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareVoluntario(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareVoluntario(entity1, entity2);
        const compareResult2 = service.compareVoluntario(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareVoluntario(entity1, entity2);
        const compareResult2 = service.compareVoluntario(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareVoluntario(entity1, entity2);
        const compareResult2 = service.compareVoluntario(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
