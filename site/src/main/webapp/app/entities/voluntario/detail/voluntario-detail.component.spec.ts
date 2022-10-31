import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { VoluntarioDetailComponent } from './voluntario-detail.component';

describe('Voluntario Management Detail Component', () => {
  let comp: VoluntarioDetailComponent;
  let fixture: ComponentFixture<VoluntarioDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [VoluntarioDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ voluntario: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(VoluntarioDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(VoluntarioDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load voluntario on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.voluntario).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
