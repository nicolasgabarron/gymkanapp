import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../equipo.test-samples';

import { EquipoFormService } from './equipo-form.service';

describe('Equipo Form Service', () => {
  let service: EquipoFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(EquipoFormService);
  });

  describe('Service methods', () => {
    describe('createEquipoFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createEquipoFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            identificador: expect.any(Object),
            nombre: expect.any(Object),
            cantidadIntegrantes: expect.any(Object),
          })
        );
      });

      it('passing IEquipo should create a new form with FormGroup', () => {
        const formGroup = service.createEquipoFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            identificador: expect.any(Object),
            nombre: expect.any(Object),
            cantidadIntegrantes: expect.any(Object),
          })
        );
      });
    });

    describe('getEquipo', () => {
      it('should return NewEquipo for default Equipo initial value', () => {
        // eslint-disable-next-line @typescript-eslint/no-unused-vars
        const formGroup = service.createEquipoFormGroup(sampleWithNewData);

        const equipo = service.getEquipo(formGroup) as any;

        expect(equipo).toMatchObject(sampleWithNewData);
      });

      it('should return NewEquipo for empty Equipo initial value', () => {
        const formGroup = service.createEquipoFormGroup();

        const equipo = service.getEquipo(formGroup) as any;

        expect(equipo).toMatchObject({});
      });

      it('should return IEquipo', () => {
        const formGroup = service.createEquipoFormGroup(sampleWithRequiredData);

        const equipo = service.getEquipo(formGroup) as any;

        expect(equipo).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IEquipo should not enable id FormControl', () => {
        const formGroup = service.createEquipoFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewEquipo should disable id FormControl', () => {
        const formGroup = service.createEquipoFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
