import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { PasoControlDetailComponent } from './paso-control-detail.component';

describe('PasoControl Management Detail Component', () => {
  let comp: PasoControlDetailComponent;
  let fixture: ComponentFixture<PasoControlDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [PasoControlDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ pasoControl: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(PasoControlDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(PasoControlDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load pasoControl on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.pasoControl).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
