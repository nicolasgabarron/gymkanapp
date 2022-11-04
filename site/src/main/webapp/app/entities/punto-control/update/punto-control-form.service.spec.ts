import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../punto-control.test-samples';

import { PuntoControlFormService } from './punto-control-form.service';

describe('PuntoControl Form Service', () => {
  let service: PuntoControlFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(PuntoControlFormService);
  });

  describe('Service methods', () => {
    describe('createPuntoControlFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createPuntoControlFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            identificador: expect.any(Object),
            orden: expect.any(Object),
            nombre: expect.any(Object),
            direccion: expect.any(Object),
          })
        );
      });

      it('passing IPuntoControl should create a new form with FormGroup', () => {
        const formGroup = service.createPuntoControlFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            identificador: expect.any(Object),
            orden: expect.any(Object),
            nombre: expect.any(Object),
            direccion: expect.any(Object),
          })
        );
      });
    });

    describe('getPuntoControl', () => {
      it('should return NewPuntoControl for default PuntoControl initial value', () => {
        // eslint-disable-next-line @typescript-eslint/no-unused-vars
        const formGroup = service.createPuntoControlFormGroup(sampleWithNewData);

        const puntoControl = service.getPuntoControl(formGroup) as any;

        expect(puntoControl).toMatchObject(sampleWithNewData);
      });

      it('should return NewPuntoControl for empty PuntoControl initial value', () => {
        const formGroup = service.createPuntoControlFormGroup();

        const puntoControl = service.getPuntoControl(formGroup) as any;

        expect(puntoControl).toMatchObject({});
      });

      it('should return IPuntoControl', () => {
        const formGroup = service.createPuntoControlFormGroup(sampleWithRequiredData);

        const puntoControl = service.getPuntoControl(formGroup) as any;

        expect(puntoControl).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IPuntoControl should not enable id FormControl', () => {
        const formGroup = service.createPuntoControlFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewPuntoControl should disable id FormControl', () => {
        const formGroup = service.createPuntoControlFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
