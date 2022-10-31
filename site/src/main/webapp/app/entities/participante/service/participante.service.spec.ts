import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { DATE_FORMAT } from 'app/config/input.constants';
import { IParticipante } from '../participante.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../participante.test-samples';

import { ParticipanteService, RestParticipante } from './participante.service';

const requireRestSample: RestParticipante = {
  ...sampleWithRequiredData,
  fechaNacimiento: sampleWithRequiredData.fechaNacimiento?.format(DATE_FORMAT),
};

describe('Participante Service', () => {
  let service: ParticipanteService;
  let httpMock: HttpTestingController;
  let expectedResult: IParticipante | IParticipante[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(ParticipanteService);
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

    it('should create a Participante', () => {
      // eslint-disable-next-line @typescript-eslint/no-unused-vars
      const participante = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(participante).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Participante', () => {
      const participante = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(participante).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Participante', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Participante', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a Participante', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addParticipanteToCollectionIfMissing', () => {
      it('should add a Participante to an empty array', () => {
        const participante: IParticipante = sampleWithRequiredData;
        expectedResult = service.addParticipanteToCollectionIfMissing([], participante);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(participante);
      });

      it('should not add a Participante to an array that contains it', () => {
        const participante: IParticipante = sampleWithRequiredData;
        const participanteCollection: IParticipante[] = [
          {
            ...participante,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addParticipanteToCollectionIfMissing(participanteCollection, participante);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Participante to an array that doesn't contain it", () => {
        const participante: IParticipante = sampleWithRequiredData;
        const participanteCollection: IParticipante[] = [sampleWithPartialData];
        expectedResult = service.addParticipanteToCollectionIfMissing(participanteCollection, participante);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(participante);
      });

      it('should add only unique Participante to an array', () => {
        const participanteArray: IParticipante[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const participanteCollection: IParticipante[] = [sampleWithRequiredData];
        expectedResult = service.addParticipanteToCollectionIfMissing(participanteCollection, ...participanteArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const participante: IParticipante = sampleWithRequiredData;
        const participante2: IParticipante = sampleWithPartialData;
        expectedResult = service.addParticipanteToCollectionIfMissing([], participante, participante2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(participante);
        expect(expectedResult).toContain(participante2);
      });

      it('should accept null and undefined values', () => {
        const participante: IParticipante = sampleWithRequiredData;
        expectedResult = service.addParticipanteToCollectionIfMissing([], null, participante, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(participante);
      });

      it('should return initial array if no Participante is added', () => {
        const participanteCollection: IParticipante[] = [sampleWithRequiredData];
        expectedResult = service.addParticipanteToCollectionIfMissing(participanteCollection, undefined, null);
        expect(expectedResult).toEqual(participanteCollection);
      });
    });

    describe('compareParticipante', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareParticipante(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareParticipante(entity1, entity2);
        const compareResult2 = service.compareParticipante(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareParticipante(entity1, entity2);
        const compareResult2 = service.compareParticipante(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareParticipante(entity1, entity2);
        const compareResult2 = service.compareParticipante(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
