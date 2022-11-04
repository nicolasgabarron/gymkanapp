import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../participante.test-samples';

import { ParticipanteFormService } from './participante-form.service';

describe('Participante Form Service', () => {
  let service: ParticipanteFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(ParticipanteFormService);
  });

  describe('Service methods', () => {
    describe('createParticipanteFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createParticipanteFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            dni: expect.any(Object),
            nombre: expect.any(Object),
            apellidos: expect.any(Object),
            fechaNacimiento: expect.any(Object),
            usuarioApp: expect.any(Object),
            equipo: expect.any(Object),
          })
        );
      });

      it('passing IParticipante should create a new form with FormGroup', () => {
        const formGroup = service.createParticipanteFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            dni: expect.any(Object),
            nombre: expect.any(Object),
            apellidos: expect.any(Object),
            fechaNacimiento: expect.any(Object),
            usuarioApp: expect.any(Object),
            equipo: expect.any(Object),
          })
        );
      });
    });

    describe('getParticipante', () => {
      it('should return NewParticipante for default Participante initial value', () => {
        // eslint-disable-next-line @typescript-eslint/no-unused-vars
        const formGroup = service.createParticipanteFormGroup(sampleWithNewData);

        const participante = service.getParticipante(formGroup) as any;

        expect(participante).toMatchObject(sampleWithNewData);
      });

      it('should return NewParticipante for empty Participante initial value', () => {
        const formGroup = service.createParticipanteFormGroup();

        const participante = service.getParticipante(formGroup) as any;

        expect(participante).toMatchObject({});
      });

      it('should return IParticipante', () => {
        const formGroup = service.createParticipanteFormGroup(sampleWithRequiredData);

        const participante = service.getParticipante(formGroup) as any;

        expect(participante).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IParticipante should not enable id FormControl', () => {
        const formGroup = service.createParticipanteFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewParticipante should disable id FormControl', () => {
        const formGroup = service.createParticipanteFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
