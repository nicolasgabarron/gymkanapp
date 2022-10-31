import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { EquipoFormService, EquipoFormGroup } from './equipo-form.service';
import { IEquipo } from '../equipo.model';
import { EquipoService } from '../service/equipo.service';

@Component({
  selector: 'jhi-equipo-update',
  templateUrl: './equipo-update.component.html',
})
export class EquipoUpdateComponent implements OnInit {
  isSaving = false;
  equipo: IEquipo | null = null;

  editForm: EquipoFormGroup = this.equipoFormService.createEquipoFormGroup();

  constructor(
    protected equipoService: EquipoService,
    protected equipoFormService: EquipoFormService,
    protected activatedRoute: ActivatedRoute
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ equipo }) => {
      this.equipo = equipo;
      if (equipo) {
        this.updateForm(equipo);
      }
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const equipo = this.equipoFormService.getEquipo(this.editForm);
    if (equipo.id !== null) {
      this.subscribeToSaveResponse(this.equipoService.update(equipo));
    } else {
      this.subscribeToSaveResponse(this.equipoService.create(equipo));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IEquipo>>): void {
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

  protected updateForm(equipo: IEquipo): void {
    this.equipo = equipo;
    this.equipoFormService.resetForm(this.editForm, equipo);
  }
}
