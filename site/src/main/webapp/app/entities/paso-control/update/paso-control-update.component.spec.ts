import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { PasoControlFormService } from './paso-control-form.service';
import { PasoControlService } from '../service/paso-control.service';
import { IPasoControl } from '../paso-control.model';
import { IEquipo } from 'app/entities/equipo/equipo.model';
import { EquipoService } from 'app/entities/equipo/service/equipo.service';
import { IPuntoControl } from 'app/entities/punto-control/punto-control.model';
import { PuntoControlService } from 'app/entities/punto-control/service/punto-control.service';
import { IVoluntario } from 'app/entities/voluntario/voluntario.model';
import { VoluntarioService } from 'app/entities/voluntario/service/voluntario.service';

import { PasoControlUpdateComponent } from './paso-control-update.component';

describe('PasoControl Management Update Component', () => {
  let comp: PasoControlUpdateComponent;
  let fixture: ComponentFixture<PasoControlUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let pasoControlFormService: PasoControlFormService;
  let pasoControlService: PasoControlService;
  let equipoService: EquipoService;
  let puntoControlService: PuntoControlService;
  let voluntarioService: VoluntarioService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [PasoControlUpdateComponent],
      providers: [
        FormBuilder,
        {
          provide: ActivatedRoute,
          useValue: {
            params: from([{}]),
          },
        },
      ],
    })
      .overrideTemplate(PasoControlUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(PasoControlUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    pasoControlFormService = TestBed.inject(PasoControlFormService);
    pasoControlService = TestBed.inject(PasoControlService);
    equipoService = TestBed.inject(EquipoService);
    puntoControlService = TestBed.inject(PuntoControlService);
    voluntarioService = TestBed.inject(VoluntarioService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Equipo query and add missing value', () => {
      const pasoControl: IPasoControl = { id: 456 };
      const equipo: IEquipo = { id: 19249 };
      pasoControl.equipo = equipo;

      const equipoCollection: IEquipo[] = [{ id: 74770 }];
      jest.spyOn(equipoService, 'query').mockReturnValue(of(new HttpResponse({ body: equipoCollection })));
      const additionalEquipos = [equipo];
      const expectedCollection: IEquipo[] = [...additionalEquipos, ...equipoCollection];
      jest.spyOn(equipoService, 'addEquipoToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ pasoControl });
      comp.ngOnInit();

      expect(equipoService.query).toHaveBeenCalled();
      expect(equipoService.addEquipoToCollectionIfMissing).toHaveBeenCalledWith(
        equipoCollection,
        ...additionalEquipos.map(expect.objectContaining)
      );
      expect(comp.equiposSharedCollection).toEqual(expectedCollection);
    });

    it('Should call PuntoControl query and add missing value', () => {
      const pasoControl: IPasoControl = { id: 456 };
      const puntoControl: IPuntoControl = { id: 80231 };
      pasoControl.puntoControl = puntoControl;

      const puntoControlCollection: IPuntoControl[] = [{ id: 72276 }];
      jest.spyOn(puntoControlService, 'query').mockReturnValue(of(new HttpResponse({ body: puntoControlCollection })));
      const additionalPuntoControls = [puntoControl];
      const expectedCollection: IPuntoControl[] = [...additionalPuntoControls, ...puntoControlCollection];
      jest.spyOn(puntoControlService, 'addPuntoControlToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ pasoControl });
      comp.ngOnInit();

      expect(puntoControlService.query).toHaveBeenCalled();
      expect(puntoControlService.addPuntoControlToCollectionIfMissing).toHaveBeenCalledWith(
        puntoControlCollection,
        ...additionalPuntoControls.map(expect.objectContaining)
      );
      expect(comp.puntoControlsSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Voluntario query and add missing value', () => {
      const pasoControl: IPasoControl = { id: 456 };
      const validadoPor: IVoluntario = { id: 58086 };
      pasoControl.validadoPor = validadoPor;

      const voluntarioCollection: IVoluntario[] = [{ id: 80393 }];
      jest.spyOn(voluntarioService, 'query').mockReturnValue(of(new HttpResponse({ body: voluntarioCollection })));
      const additionalVoluntarios = [validadoPor];
      const expectedCollection: IVoluntario[] = [...additionalVoluntarios, ...voluntarioCollection];
      jest.spyOn(voluntarioService, 'addVoluntarioToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ pasoControl });
      comp.ngOnInit();

      expect(voluntarioService.query).toHaveBeenCalled();
      expect(voluntarioService.addVoluntarioToCollectionIfMissing).toHaveBeenCalledWith(
        voluntarioCollection,
        ...additionalVoluntarios.map(expect.objectContaining)
      );
      expect(comp.voluntariosSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const pasoControl: IPasoControl = { id: 456 };
      const equipo: IEquipo = { id: 66969 };
      pasoControl.equipo = equipo;
      const puntoControl: IPuntoControl = { id: 65548 };
      pasoControl.puntoControl = puntoControl;
      const validadoPor: IVoluntario = { id: 6613 };
      pasoControl.validadoPor = validadoPor;

      activatedRoute.data = of({ pasoControl });
      comp.ngOnInit();

      expect(comp.equiposSharedCollection).toContain(equipo);
      expect(comp.puntoControlsSharedCollection).toContain(puntoControl);
      expect(comp.voluntariosSharedCollection).toContain(validadoPor);
      expect(comp.pasoControl).toEqual(pasoControl);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IPasoControl>>();
      const pasoControl = { id: 123 };
      jest.spyOn(pasoControlFormService, 'getPasoControl').mockReturnValue(pasoControl);
      jest.spyOn(pasoControlService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ pasoControl });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: pasoControl }));
      saveSubject.complete();

      // THEN
      expect(pasoControlFormService.getPasoControl).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(pasoControlService.update).toHaveBeenCalledWith(expect.objectContaining(pasoControl));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IPasoControl>>();
      const pasoControl = { id: 123 };
      jest.spyOn(pasoControlFormService, 'getPasoControl').mockReturnValue({ id: null });
      jest.spyOn(pasoControlService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ pasoControl: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: pasoControl }));
      saveSubject.complete();

      // THEN
      expect(pasoControlFormService.getPasoControl).toHaveBeenCalled();
      expect(pasoControlService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IPasoControl>>();
      const pasoControl = { id: 123 };
      jest.spyOn(pasoControlService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ pasoControl });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(pasoControlService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareEquipo', () => {
      it('Should forward to equipoService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(equipoService, 'compareEquipo');
        comp.compareEquipo(entity, entity2);
        expect(equipoService.compareEquipo).toHaveBeenCalledWith(entity, entity2);
      });
    });

    describe('comparePuntoControl', () => {
      it('Should forward to puntoControlService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(puntoControlService, 'comparePuntoControl');
        comp.comparePuntoControl(entity, entity2);
        expect(puntoControlService.comparePuntoControl).toHaveBeenCalledWith(entity, entity2);
      });
    });

    describe('compareVoluntario', () => {
      it('Should forward to voluntarioService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(voluntarioService, 'compareVoluntario');
        comp.compareVoluntario(entity, entity2);
        expect(voluntarioService.compareVoluntario).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
