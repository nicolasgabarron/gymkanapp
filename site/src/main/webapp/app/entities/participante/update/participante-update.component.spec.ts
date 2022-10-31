import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { ParticipanteFormService } from './participante-form.service';
import { ParticipanteService } from '../service/participante.service';
import { IParticipante } from '../participante.model';

import { IUser } from 'app/entities/user/user.model';
import { UserService } from 'app/entities/user/user.service';
import { IEquipo } from 'app/entities/equipo/equipo.model';
import { EquipoService } from 'app/entities/equipo/service/equipo.service';

import { ParticipanteUpdateComponent } from './participante-update.component';

describe('Participante Management Update Component', () => {
  let comp: ParticipanteUpdateComponent;
  let fixture: ComponentFixture<ParticipanteUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let participanteFormService: ParticipanteFormService;
  let participanteService: ParticipanteService;
  let userService: UserService;
  let equipoService: EquipoService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [ParticipanteUpdateComponent],
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
      .overrideTemplate(ParticipanteUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(ParticipanteUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    participanteFormService = TestBed.inject(ParticipanteFormService);
    participanteService = TestBed.inject(ParticipanteService);
    userService = TestBed.inject(UserService);
    equipoService = TestBed.inject(EquipoService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call User query and add missing value', () => {
      const participante: IParticipante = { id: 456 };
      const usuarioApp: IUser = { id: 13369 };
      participante.usuarioApp = usuarioApp;

      const userCollection: IUser[] = [{ id: 44926 }];
      jest.spyOn(userService, 'query').mockReturnValue(of(new HttpResponse({ body: userCollection })));
      const additionalUsers = [usuarioApp];
      const expectedCollection: IUser[] = [...additionalUsers, ...userCollection];
      jest.spyOn(userService, 'addUserToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ participante });
      comp.ngOnInit();

      expect(userService.query).toHaveBeenCalled();
      expect(userService.addUserToCollectionIfMissing).toHaveBeenCalledWith(
        userCollection,
        ...additionalUsers.map(expect.objectContaining)
      );
      expect(comp.usersSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Equipo query and add missing value', () => {
      const participante: IParticipante = { id: 456 };
      const equipo: IEquipo = { id: 85367 };
      participante.equipo = equipo;

      const equipoCollection: IEquipo[] = [{ id: 17539 }];
      jest.spyOn(equipoService, 'query').mockReturnValue(of(new HttpResponse({ body: equipoCollection })));
      const additionalEquipos = [equipo];
      const expectedCollection: IEquipo[] = [...additionalEquipos, ...equipoCollection];
      jest.spyOn(equipoService, 'addEquipoToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ participante });
      comp.ngOnInit();

      expect(equipoService.query).toHaveBeenCalled();
      expect(equipoService.addEquipoToCollectionIfMissing).toHaveBeenCalledWith(
        equipoCollection,
        ...additionalEquipos.map(expect.objectContaining)
      );
      expect(comp.equiposSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const participante: IParticipante = { id: 456 };
      const usuarioApp: IUser = { id: 50322 };
      participante.usuarioApp = usuarioApp;
      const equipo: IEquipo = { id: 69796 };
      participante.equipo = equipo;

      activatedRoute.data = of({ participante });
      comp.ngOnInit();

      expect(comp.usersSharedCollection).toContain(usuarioApp);
      expect(comp.equiposSharedCollection).toContain(equipo);
      expect(comp.participante).toEqual(participante);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IParticipante>>();
      const participante = { id: 123 };
      jest.spyOn(participanteFormService, 'getParticipante').mockReturnValue(participante);
      jest.spyOn(participanteService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ participante });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: participante }));
      saveSubject.complete();

      // THEN
      expect(participanteFormService.getParticipante).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(participanteService.update).toHaveBeenCalledWith(expect.objectContaining(participante));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IParticipante>>();
      const participante = { id: 123 };
      jest.spyOn(participanteFormService, 'getParticipante').mockReturnValue({ id: null });
      jest.spyOn(participanteService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ participante: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: participante }));
      saveSubject.complete();

      // THEN
      expect(participanteFormService.getParticipante).toHaveBeenCalled();
      expect(participanteService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IParticipante>>();
      const participante = { id: 123 };
      jest.spyOn(participanteService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ participante });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(participanteService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareUser', () => {
      it('Should forward to userService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(userService, 'compareUser');
        comp.compareUser(entity, entity2);
        expect(userService.compareUser).toHaveBeenCalledWith(entity, entity2);
      });
    });

    describe('compareEquipo', () => {
      it('Should forward to equipoService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(equipoService, 'compareEquipo');
        comp.compareEquipo(entity, entity2);
        expect(equipoService.compareEquipo).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
