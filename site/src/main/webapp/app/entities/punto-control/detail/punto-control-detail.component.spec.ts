import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { PuntoControlDetailComponent } from './punto-control-detail.component';

describe('PuntoControl Management Detail Component', () => {
  let comp: PuntoControlDetailComponent;
  let fixture: ComponentFixture<PuntoControlDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [PuntoControlDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ puntoControl: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(PuntoControlDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(PuntoControlDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load puntoControl on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.puntoControl).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
