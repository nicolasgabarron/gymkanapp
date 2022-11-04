import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { EquipoDetailComponent } from './equipo-detail.component';

describe('Equipo Management Detail Component', () => {
  let comp: EquipoDetailComponent;
  let fixture: ComponentFixture<EquipoDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [EquipoDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ equipo: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(EquipoDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(EquipoDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load equipo on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.equipo).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
