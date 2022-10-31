import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IPuntoControl } from '../punto-control.model';

@Component({
  selector: 'jhi-punto-control-detail',
  templateUrl: './punto-control-detail.component.html',
})
export class PuntoControlDetailComponent implements OnInit {
  puntoControl: IPuntoControl | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ puntoControl }) => {
      this.puntoControl = puntoControl;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
