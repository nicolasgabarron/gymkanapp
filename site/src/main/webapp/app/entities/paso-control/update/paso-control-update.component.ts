import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { PasoControlFormService, PasoControlFormGroup } from './paso-control-form.service';
import { IPasoControl } from '../paso-control.model';
import { PasoControlService } from '../service/paso-control.service';
import { IEquipo } from 'app/entities/equipo/equipo.model';
import { EquipoService } from 'app/entities/equipo/service/equipo.service';
import { IPuntoControl } from 'app/entities/punto-control/punto-control.model';
import { PuntoControlService } from 'app/entities/punto-control/service/punto-control.service';
import { IVoluntario } from 'app/entities/voluntario/voluntario.model';
import { VoluntarioService } from 'app/entities/voluntario/service/voluntario.service';

@Component({
  selector: 'jhi-paso-control-update',
  templateUrl: './paso-control-update.component.html',
  styleUrls: ['./paso-control.component.scss']
})
export class PasoControlUpdateComponent implements OnInit {
  isSaving = false;
  pasoControl: IPasoControl | null = null;

  equiposSharedCollection: IEquipo[] = [];
  puntoControlsSharedCollection: IPuntoControl[] = [];
  voluntariosSharedCollection: IVoluntario[] = [];

  editForm: PasoControlFormGroup = this.pasoControlFormService.createPasoControlFormGroup();

  constructor(
    protected pasoControlService: PasoControlService,
    protected pasoControlFormService: PasoControlFormService,
    protected equipoService: EquipoService,
    protected puntoControlService: PuntoControlService,
    protected voluntarioService: VoluntarioService,
    protected activatedRoute: ActivatedRoute
  ) {}

  compareEquipo = (o1: IEquipo | null, o2: IEquipo | null): boolean => this.equipoService.compareEquipo(o1, o2);

  comparePuntoControl = (o1: IPuntoControl | null, o2: IPuntoControl | null): boolean =>
    this.puntoControlService.comparePuntoControl(o1, o2);

  compareVoluntario = (o1: IVoluntario | null, o2: IVoluntario | null): boolean => this.voluntarioService.compareVoluntario(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ pasoControl }) => {
      this.pasoControl = pasoControl;
      if (pasoControl) {
        this.updateForm(pasoControl);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const pasoControl = this.pasoControlFormService.getPasoControl(this.editForm);
    if (pasoControl.id !== null) {
      this.subscribeToSaveResponse(this.pasoControlService.update(pasoControl));
    } else {
      this.subscribeToSaveResponse(this.pasoControlService.create(pasoControl));
    }
  }

  searchEquipo(event: any): void {
    const IdentificadorNombreEquipo = event.query;

    const queryIdentificadorNombre: any = {
      sort:['nombre,asc']
    };

    if(IdentificadorNombreEquipo){
      queryIdentificadorNombre["identificador.contains"] = IdentificadorNombreEquipo;
      // queryIdentificadorNombre["nombre.contains"] = IdentificadorNombreEquipo;
    }

    this.equipoService.query(queryIdentificadorNombre)
    .pipe(map((res: HttpResponse<IEquipo[]>) => res.body ?? []))
    .subscribe((equipos: IEquipo[]) => (this.equiposSharedCollection = equipos));
  }

  searchPuntoControl(event: any): void{
    const nombrePuntoControl = event.query;

    const queryPControlObject: any = {
      sort:['nombre,asc']
    };

    if(nombrePuntoControl){
      queryPControlObject["nombre.contains"] = nombrePuntoControl;
    }

    this.puntoControlService.query(queryPControlObject)
    .pipe(map((res: HttpResponse<IPuntoControl[]>) => res.body ?? []))
    .subscribe((puntos: IPuntoControl[]) => (this.puntoControlsSharedCollection = puntos));
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IPasoControl>>): void {
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

  protected updateForm(pasoControl: IPasoControl): void {
    this.pasoControl = pasoControl;
    this.pasoControlFormService.resetForm(this.editForm, pasoControl);

    this.equiposSharedCollection = this.equipoService.addEquipoToCollectionIfMissing<IEquipo>(
      this.equiposSharedCollection,
      pasoControl.equipo
    );
    this.puntoControlsSharedCollection = this.puntoControlService.addPuntoControlToCollectionIfMissing<IPuntoControl>(
      this.puntoControlsSharedCollection,
      pasoControl.puntoControl
    );
    this.voluntariosSharedCollection = this.voluntarioService.addVoluntarioToCollectionIfMissing<IVoluntario>(
      this.voluntariosSharedCollection,
      pasoControl.validadoPor
    );
  }

  protected loadRelationshipsOptions(): void {
    this.equipoService
      .query()
      .pipe(map((res: HttpResponse<IEquipo[]>) => res.body ?? []))
      .pipe(map((equipos: IEquipo[]) => this.equipoService.addEquipoToCollectionIfMissing<IEquipo>(equipos, this.pasoControl?.equipo)))
      .subscribe((equipos: IEquipo[]) => (this.equiposSharedCollection = equipos));

    this.puntoControlService
      .query()
      .pipe(map((res: HttpResponse<IPuntoControl[]>) => res.body ?? []))
      .pipe(
        map((puntoControls: IPuntoControl[]) =>
          this.puntoControlService.addPuntoControlToCollectionIfMissing<IPuntoControl>(puntoControls, this.pasoControl?.puntoControl)
        )
      )
      .subscribe((puntoControls: IPuntoControl[]) => (this.puntoControlsSharedCollection = puntoControls));

    this.voluntarioService
      .query()
      .pipe(map((res: HttpResponse<IVoluntario[]>) => res.body ?? []))
      .pipe(
        map((voluntarios: IVoluntario[]) =>
          this.voluntarioService.addVoluntarioToCollectionIfMissing<IVoluntario>(voluntarios, this.pasoControl?.validadoPor)
        )
      )
      .subscribe((voluntarios: IVoluntario[]) => (this.voluntariosSharedCollection = voluntarios));
  }
}
