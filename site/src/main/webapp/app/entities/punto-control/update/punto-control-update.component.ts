import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { PuntoControlFormService, PuntoControlFormGroup } from './punto-control-form.service';
import { IPuntoControl } from '../punto-control.model';
import { PuntoControlService } from '../service/punto-control.service';

@Component({
  selector: 'jhi-punto-control-update',
  templateUrl: './punto-control-update.component.html',
})
export class PuntoControlUpdateComponent implements OnInit {
  isSaving = false;
  puntoControl: IPuntoControl | null = null;

  editForm: PuntoControlFormGroup = this.puntoControlFormService.createPuntoControlFormGroup();

  constructor(
    protected puntoControlService: PuntoControlService,
    protected puntoControlFormService: PuntoControlFormService,
    protected activatedRoute: ActivatedRoute
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ puntoControl }) => {
      this.puntoControl = puntoControl;
      if (puntoControl) {
        this.updateForm(puntoControl);
      }
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const puntoControl = this.puntoControlFormService.getPuntoControl(this.editForm);
    if (puntoControl.id !== null) {
      this.subscribeToSaveResponse(this.puntoControlService.update(puntoControl));
    } else {
      this.subscribeToSaveResponse(this.puntoControlService.create(puntoControl));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IPuntoControl>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(puntoControl: IPuntoControl): void {
    this.puntoControl = puntoControl;
    this.puntoControlFormService.resetForm(this.editForm, puntoControl);
  }
}
