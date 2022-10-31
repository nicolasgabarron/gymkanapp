import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { VoluntarioFormService } from './voluntario-form.service';
import { VoluntarioService } from '../service/voluntario.service';
import { IVoluntario } from '../voluntario.model';

import { IUser } from 'app/entities/user/user.model';
import { UserService } from 'app/entities/user/user.service';
import { IPuntoControl } from 'app/entities/punto-control/punto-control.model';
import { PuntoControlService } from 'app/entities/punto-control/service/punto-control.service';

import { VoluntarioUpdateComponent } from './voluntario-update.component';

describe('Voluntario Management Update Component', () => {
  let comp: VoluntarioUpdateComponent;
  let fixture: ComponentFixture<VoluntarioUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let voluntarioFormService: VoluntarioFormService;
  let voluntarioService: VoluntarioService;
  let userService: UserService;
  let puntoControlService: PuntoControlService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [VoluntarioUpdateComponent],
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
      .overrideTemplate(VoluntarioUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(VoluntarioUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    voluntarioFormService = TestBed.inject(VoluntarioFormService);
    voluntarioService = TestBed.inject(VoluntarioService);
    userService = TestBed.inject(UserService);
    puntoControlService = TestBed.inject(PuntoControlService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call User query and add missing value', () => {
      const voluntario: IVoluntario = { id: 456 };
      const usuarioApp: IUser = { id: 38154 };
      voluntario.usuarioApp = usuarioApp;

      const userCollection: IUser[] = [{ id: 76809 }];
      jest.spyOn(userService, 'query').mockReturnValue(of(new HttpResponse({ body: userCollection })));
      const additionalUsers = [usuarioApp];
      const expectedCollection: IUser[] = [...additionalUsers, ...userCollection];
      jest.spyOn(userService, 'addUserToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ voluntario });
      comp.ngOnInit();

      expect(userService.query).toHaveBeenCalled();
      expect(userService.addUserToCollectionIfMissing).toHaveBeenCalledWith(
        userCollection,
        ...additionalUsers.map(expect.objectContaining)
      );
      expect(comp.usersSharedCollection).toEqual(expectedCollection);
    });

    it('Should call PuntoControl query and add missing value', () => {
      const voluntario: IVoluntario = { id: 456 };
      const puntoControl: IPuntoControl = { id: 1690 };
      voluntario.puntoControl = puntoControl;

      const puntoControlCollection: IPuntoControl[] = [{ id: 86728 }];
      jest.spyOn(puntoControlService, 'query').mockReturnValue(of(new HttpResponse({ body: puntoControlCollection })));
      const additionalPuntoControls = [puntoControl];
      const expectedCollection: IPuntoControl[] = [...additionalPuntoControls, ...puntoControlCollection];
      jest.spyOn(puntoControlService, 'addPuntoControlToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ voluntario });
      comp.ngOnInit();

      expect(puntoControlService.query).toHaveBeenCalled();
      expect(puntoControlService.addPuntoControlToCollectionIfMissing).toHaveBeenCalledWith(
        puntoControlCollection,
        ...additionalPuntoControls.map(expect.objectContaining)
      );
      expect(comp.puntoControlsSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const voluntario: IVoluntario = { id: 456 };
      const usuarioApp: IUser = { id: 99782 };
      voluntario.usuarioApp = usuarioApp;
      const puntoControl: IPuntoControl = { id: 70970 };
      voluntario.puntoControl = puntoControl;

      activatedRoute.data = of({ voluntario });
      comp.ngOnInit();

      expect(comp.usersSharedCollection).toContain(usuarioApp);
      expect(comp.puntoControlsSharedCollection).toContain(puntoControl);
      expect(comp.voluntario).toEqual(voluntario);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IVoluntario>>();
      const voluntario = { id: 123 };
      jest.spyOn(voluntarioFormService, 'getVoluntario').mockReturnValue(voluntario);
      jest.spyOn(voluntarioService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ voluntario });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: voluntario }));
      saveSubject.complete();

      // THEN
      expect(voluntarioFormService.getVoluntario).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(voluntarioService.update).toHaveBeenCalledWith(expect.objectContaining(voluntario));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IVoluntario>>();
      const voluntario = { id: 123 };
      jest.spyOn(voluntarioFormService, 'getVoluntario').mockReturnValue({ id: null });
      jest.spyOn(voluntarioService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ voluntario: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: voluntario }));
      saveSubject.complete();

      // THEN
      expect(voluntarioFormService.getVoluntario).toHaveBeenCalled();
      expect(voluntarioService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IVoluntario>>();
      const voluntario = { id: 123 };
      jest.spyOn(voluntarioService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ voluntario });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(voluntarioService.update).toHaveBeenCalled();
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

    describe('comparePuntoControl', () => {
      it('Should forward to puntoControlService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(puntoControlService, 'comparePuntoControl');
        comp.comparePuntoControl(entity, entity2);
        expect(puntoControlService.comparePuntoControl).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
