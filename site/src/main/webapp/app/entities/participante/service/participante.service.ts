import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { DATE_FORMAT } from 'app/config/input.constants';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IParticipante, NewParticipante } from '../participante.model';

export type PartialUpdateParticipante = Partial<IParticipante> & Pick<IParticipante, 'id'>;

type RestOf<T extends IParticipante | NewParticipante> = Omit<T, 'fechaNacimiento'> & {
  fechaNacimiento?: string | null;
};

export type RestParticipante = RestOf<IParticipante>;

export type NewRestParticipante = RestOf<NewParticipante>;

export type PartialUpdateRestParticipante = RestOf<PartialUpdateParticipante>;

export type EntityResponseType = HttpResponse<IParticipante>;
export type EntityArrayResponseType = HttpResponse<IParticipante[]>;

@Injectable({ providedIn: 'root' })
export class ParticipanteService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/participantes');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(participante: NewParticipante): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(participante);
    return this.http
      .post<RestParticipante>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(participante: IParticipante): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(participante);
    return this.http
      .put<RestParticipante>(`${this.resourceUrl}/${this.getParticipanteIdentifier(participante)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(participante: PartialUpdateParticipante): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(participante);
    return this.http
      .patch<RestParticipante>(`${this.resourceUrl}/${this.getParticipanteIdentifier(participante)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestParticipante>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestParticipante[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getParticipanteIdentifier(participante: Pick<IParticipante, 'id'>): number {
    return participante.id;
  }

  compareParticipante(o1: Pick<IParticipante, 'id'> | null, o2: Pick<IParticipante, 'id'> | null): boolean {
    return o1 && o2 ? this.getParticipanteIdentifier(o1) === this.getParticipanteIdentifier(o2) : o1 === o2;
  }

  addParticipanteToCollectionIfMissing<Type extends Pick<IParticipante, 'id'>>(
    participanteCollection: Type[],
    ...participantesToCheck: (Type | null | undefined)[]
  ): Type[] {
    const participantes: Type[] = participantesToCheck.filter(isPresent);
    if (participantes.length > 0) {
      const participanteCollectionIdentifiers = participanteCollection.map(
        participanteItem => this.getParticipanteIdentifier(participanteItem)!
      );
      const participantesToAdd = participantes.filter(participanteItem => {
        const participanteIdentifier = this.getParticipanteIdentifier(participanteItem);
        if (participanteCollectionIdentifiers.includes(participanteIdentifier)) {
          return false;
        }
        participanteCollectionIdentifiers.push(participanteIdentifier);
        return true;
      });
      return [...participantesToAdd, ...participanteCollection];
    }
    return participanteCollection;
  }

  protected convertDateFromClient<T extends IParticipante | NewParticipante | PartialUpdateParticipante>(participante: T): RestOf<T> {
    return {
      ...participante,
      fechaNacimiento: participante.fechaNacimiento?.format(DATE_FORMAT) ?? null,
    };
  }

  protected convertDateFromServer(restParticipante: RestParticipante): IParticipante {
    return {
      ...restParticipante,
      fechaNacimiento: restParticipante.fechaNacimiento ? dayjs(restParticipante.fechaNacimiento) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestParticipante>): HttpResponse<IParticipante> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestParticipante[]>): HttpResponse<IParticipante[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
