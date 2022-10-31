import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../voluntario.test-samples';

import { VoluntarioFormService } from './voluntario-form.service';

describe('Voluntario Form Service', () => {
  let service: VoluntarioFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(VoluntarioFormService);
  });

  describe('Service methods', () => {
    describe('createVoluntarioFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createVoluntarioFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            dni: expect.any(Object),
            nombre: expect.any(Object),
            apellidos: expect.any(Object),
            fechaNacimiento: expect.any(Object),
            usuarioApp: expect.any(Object),
            puntoControl: expect.any(Object),
          })
        );
      });

      it('passing IVoluntario should create a new form with FormGroup', () => {
        const formGroup = service.createVoluntarioFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            dni: expect.any(Object),
            nombre: expect.any(Object),
            apellidos: expect.any(Object),
            fechaNacimiento: expect.any(Object),
            usuarioApp: expect.any(Object),
            puntoControl: expect.any(Object),
          })
        );
      });
    });

    describe('getVoluntario', () => {
      it('should return NewVoluntario for default Voluntario initial value', () => {
        // eslint-disable-next-line @typescript-eslint/no-unused-vars
        const formGroup = service.createVoluntarioFormGroup(sampleWithNewData);

        const voluntario = service.getVoluntario(formGroup) as any;

        expect(voluntario).toMatchObject(sampleWithNewData);
      });

      it('should return NewVoluntario for empty Voluntario initial value', () => {
        const formGroup = service.createVoluntarioFormGroup();

        const voluntario = service.getVoluntario(formGroup) as any;

        expect(voluntario).toMatchObject({});
      });

      it('should return IVoluntario', () => {
        const formGroup = service.createVoluntarioFormGroup(sampleWithRequiredData);

        const voluntario = service.getVoluntario(formGroup) as any;

        expect(voluntario).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IVoluntario should not enable id FormControl', () => {
        const formGroup = service.createVoluntarioFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewVoluntario should disable id FormControl', () => {
        const formGroup = service.createVoluntarioFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
