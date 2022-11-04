import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { ParticipanteFormService, ParticipanteFormGroup } from './participante-form.service';
import { IParticipante } from '../participante.model';
import { ParticipanteService } from '../service/participante.service';
import { IUser } from 'app/entities/user/user.model';
import { UserService } from 'app/entities/user/user.service';
import { IEquipo } from 'app/entities/equipo/equipo.model';
import { EquipoService } from 'app/entities/equipo/service/equipo.service';

@Component({
  selector: 'jhi-participante-update',
  templateUrl: './participante-update.component.html',
  styleUrls: ['./participante-update.component.scss']
})
export class ParticipanteUpdateComponent implements OnInit {
  isSaving = false;
  participante: IParticipante | null = null;

  usersSharedCollection: IUser[] = [];
  equiposSharedCollection: IEquipo[] = [];

  editForm: ParticipanteFormGroup = this.participanteFormService.createParticipanteFormGroup();

  constructor(
    protected participanteService: ParticipanteService,
    protected participanteFormService: ParticipanteFormService,
    protected userService: UserService,
    protected equipoService: EquipoService,
    protected activatedRoute: ActivatedRoute
  ) {}

  compareUser = (o1: IUser | null, o2: IUser | null): boolean => this.userService.compareUser(o1, o2);

  compareEquipo = (o1: IEquipo | null, o2: IEquipo | null): boolean => this.equipoService.compareEquipo(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ participante }) => {
      this.participante = participante;
      if (participante) {
        this.updateForm(participante);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const participante = this.participanteFormService.getParticipante(this.editForm);
    if (participante.id !== null) {
      this.subscribeToSaveResponse(this.participanteService.update(participante));
    } else {
      this.subscribeToSaveResponse(this.participanteService.create(participante));
    }
  }

  searchEquipo(event: any): void {
    const IdentificadorNombreEquipo = event.query;

    const queryIdentificadorNombre: any = {
      sort:['nombre,asc']
    };

    if(IdentificadorNombreEquipo){
      queryIdentificadorNombre["identificador.contains"] = IdentificadorNombreEquipo;
      queryIdentificadorNombre["nombre.contains"] = IdentificadorNombreEquipo;
    }

    this.equipoService.query(queryIdentificadorNombre)
    .pipe(map((res: HttpResponse<IEquipo[]>) => res.body ?? []))
    .subscribe((equipos: IEquipo[]) => (this.equiposSharedCollection = equipos));
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IParticipante>>): void {
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

  protected updateForm(participante: IParticipante): void {
    this.participante = participante;
    this.participanteFormService.resetForm(this.editForm, participante);

    this.usersSharedCollection = this.userService.addUserToCollectionIfMissing<IUser>(this.usersSharedCollection, participante.usuarioApp);
    this.equiposSharedCollection = this.equipoService.addEquipoToCollectionIfMissing<IEquipo>(
      this.equiposSharedCollection,
      participante.equipo
    );
  }

  protected loadRelationshipsOptions(): void {
    this.userService
      .query()
      .pipe(map((res: HttpResponse<IUser[]>) => res.body ?? []))
      .pipe(map((users: IUser[]) => this.userService.addUserToCollectionIfMissing<IUser>(users, this.participante?.usuarioApp)))
      .subscribe((users: IUser[]) => (this.usersSharedCollection = users));

    this.equipoService
      .query()
      .pipe(map((res: HttpResponse<IEquipo[]>) => res.body ?? []))
      .pipe(map((equipos: IEquipo[]) => this.equipoService.addEquipoToCollectionIfMissing<IEquipo>(equipos, this.participante?.equipo)))
      .subscribe((equipos: IEquipo[]) => (this.equiposSharedCollection = equipos));
  }
}
