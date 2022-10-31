import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IEquipo } from '../equipo.model';

@Component({
  selector: 'jhi-equipo-detail',
  templateUrl: './equipo-detail.component.html',
})
export class EquipoDetailComponent implements OnInit {
  equipo: IEquipo | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ equipo }) => {
      this.equipo = equipo;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
