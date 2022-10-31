import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IVoluntario } from '../voluntario.model';

@Component({
  selector: 'jhi-voluntario-detail',
  templateUrl: './voluntario-detail.component.html',
})
export class VoluntarioDetailComponent implements OnInit {
  voluntario: IVoluntario | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ voluntario }) => {
      this.voluntario = voluntario;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
