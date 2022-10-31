import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { VoluntarioFormService, VoluntarioFormGroup } from './voluntario-form.service';
import { IVoluntario } from '../voluntario.model';
import { VoluntarioService } from '../service/voluntario.service';
import { IUser } from 'app/entities/user/user.model';
import { UserService } from 'app/entities/user/user.service';
import { IPuntoControl } from 'app/entities/punto-control/punto-control.model';
import { PuntoControlService } from 'app/entities/punto-control/service/punto-control.service';

@Component({
  selector: 'jhi-voluntario-update',
  templateUrl: './voluntario-update.component.html',
})
export class VoluntarioUpdateComponent implements OnInit {
  isSaving = false;
  voluntario: IVoluntario | null = null;

  usersSharedCollection: IUser[] = [];
  puntoControlsSharedCollection: IPuntoControl[] = [];

  editForm: VoluntarioFormGroup = this.voluntarioFormService.createVoluntarioFormGroup();

  constructor(
    protected voluntarioService: VoluntarioService,
    protected voluntarioFormService: VoluntarioFormService,
    protected userService: UserService,
    protected puntoControlService: PuntoControlService,
    protected activatedRoute: ActivatedRoute
  ) {}

  compareUser = (o1: IUser | null, o2: IUser | null): boolean => this.userService.compareUser(o1, o2);

  comparePuntoControl = (o1: IPuntoControl | null, o2: IPuntoControl | null): boolean =>
    this.puntoControlService.comparePuntoControl(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ voluntario }) => {
      this.voluntario = voluntario;
      if (voluntario) {
        this.updateForm(voluntario);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const voluntario = this.voluntarioFormService.getVoluntario(this.editForm);
    if (voluntario.id !== null) {
      this.subscribeToSaveResponse(this.voluntarioService.update(voluntario));
    } else {
      this.subscribeToSaveResponse(this.voluntarioService.create(voluntario));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IVoluntario>>): void {
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

  protected updateForm(voluntario: IVoluntario): void {
    this.voluntario = voluntario;
    this.voluntarioFormService.resetForm(this.editForm, voluntario);

    this.usersSharedCollection = this.userService.addUserToCollectionIfMissing<IUser>(this.usersSharedCollection, voluntario.usuarioApp);
    this.puntoControlsSharedCollection = this.puntoControlService.addPuntoControlToCollectionIfMissing<IPuntoControl>(
      this.puntoControlsSharedCollection,
      voluntario.puntoControl
    );
  }

  protected loadRelationshipsOptions(): void {
    this.userService
      .query()
      .pipe(map((res: HttpResponse<IUser[]>) => res.body ?? []))
      .pipe(map((users: IUser[]) => this.userService.addUserToCollectionIfMissing<IUser>(users, this.voluntario?.usuarioApp)))
      .subscribe((users: IUser[]) => (this.usersSharedCollection = users));

    this.puntoControlService
      .query()
      .pipe(map((res: HttpResponse<IPuntoControl[]>) => res.body ?? []))
      .pipe(
        map((puntoControls: IPuntoControl[]) =>
          this.puntoControlService.addPuntoControlToCollectionIfMissing<IPuntoControl>(puntoControls, this.voluntario?.puntoControl)
        )
      )
      .subscribe((puntoControls: IPuntoControl[]) => (this.puntoControlsSharedCollection = puntoControls));
  }
}
