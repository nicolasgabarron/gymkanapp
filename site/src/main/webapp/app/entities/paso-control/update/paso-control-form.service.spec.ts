import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../paso-control.test-samples';

import { PasoControlFormService } from './paso-control-form.service';

describe('PasoControl Form Service', () => {
  let service: PasoControlFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(PasoControlFormService);
  });

  describe('Service methods', () => {
    describe('createPasoControlFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createPasoControlFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            fechaHora: expect.any(Object),
            equipo: expect.any(Object),
            puntoControl: expect.any(Object),
            validadoPor: expect.any(Object),
          })
        );
      });

      it('passing IPasoControl should create a new form with FormGroup', () => {
        const formGroup = service.createPasoControlFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            fechaHora: expect.any(Object),
            equipo: expect.any(Object),
            puntoControl: expect.any(Object),
            validadoPor: expect.any(Object),
          })
        );
      });
    });

    describe('getPasoControl', () => {
      it('should return NewPasoControl for default PasoControl initial value', () => {
        // eslint-disable-next-line @typescript-eslint/no-unused-vars
        const formGroup = service.createPasoControlFormGroup(sampleWithNewData);

        const pasoControl = service.getPasoControl(formGroup) as any;

        expect(pasoControl).toMatchObject(sampleWithNewData);
      });

      it('should return NewPasoControl for empty PasoControl initial value', () => {
        const formGroup = service.createPasoControlFormGroup();

        const pasoControl = service.getPasoControl(formGroup) as any;

        expect(pasoControl).toMatchObject({});
      });

      it('should return IPasoControl', () => {
        const formGroup = service.createPasoControlFormGroup(sampleWithRequiredData);

        const pasoControl = service.getPasoControl(formGroup) as any;

        expect(pasoControl).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IPasoControl should not enable id FormControl', () => {
        const formGroup = service.createPasoControlFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewPasoControl should disable id FormControl', () => {
        const formGroup = service.createPasoControlFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
