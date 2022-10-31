import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IPasoControl } from '../paso-control.model';

@Component({
  selector: 'jhi-paso-control-detail',
  templateUrl: './paso-control-detail.component.html',
})
export class PasoControlDetailComponent implements OnInit {
  pasoControl: IPasoControl | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ pasoControl }) => {
      this.pasoControl = pasoControl;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
