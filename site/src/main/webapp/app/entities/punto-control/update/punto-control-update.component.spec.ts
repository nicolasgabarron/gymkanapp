import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { PuntoControlFormService } from './punto-control-form.service';
import { PuntoControlService } from '../service/punto-control.service';
import { IPuntoControl } from '../punto-control.model';

import { PuntoControlUpdateComponent } from './punto-control-update.component';

describe('PuntoControl Management Update Component', () => {
  let comp: PuntoControlUpdateComponent;
  let fixture: ComponentFixture<PuntoControlUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let puntoControlFormService: PuntoControlFormService;
  let puntoControlService: PuntoControlService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [PuntoControlUpdateComponent],
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
      .overrideTemplate(PuntoControlUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(PuntoControlUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    puntoControlFormService = TestBed.inject(PuntoControlFormService);
    puntoControlService = TestBed.inject(PuntoControlService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const puntoControl: IPuntoControl = { id: 456 };

      activatedRoute.data = of({ puntoControl });
      comp.ngOnInit();

      expect(comp.puntoControl).toEqual(puntoControl);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IPuntoControl>>();
      const puntoControl = { id: 123 };
      jest.spyOn(puntoControlFormService, 'getPuntoControl').mockReturnValue(puntoControl);
      jest.spyOn(puntoControlService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ puntoControl });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: puntoControl }));
      saveSubject.complete();

      // THEN
      expect(puntoControlFormService.getPuntoControl).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(puntoControlService.update).toHaveBeenCalledWith(expect.objectContaining(puntoControl));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IPuntoControl>>();
      const puntoControl = { id: 123 };
      jest.spyOn(puntoControlFormService, 'getPuntoControl').mockReturnValue({ id: null });
      jest.spyOn(puntoControlService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ puntoControl: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: puntoControl }));
      saveSubject.complete();

      // THEN
      expect(puntoControlFormService.getPuntoControl).toHaveBeenCalled();
      expect(puntoControlService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IPuntoControl>>();
      const puntoControl = { id: 123 };
      jest.spyOn(puntoControlService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ puntoControl });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(puntoControlService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
