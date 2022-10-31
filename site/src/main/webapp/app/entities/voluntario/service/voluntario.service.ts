import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { DATE_FORMAT } from 'app/config/input.constants';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IVoluntario, NewVoluntario } from '../voluntario.model';

export type PartialUpdateVoluntario = Partial<IVoluntario> & Pick<IVoluntario, 'id'>;

type RestOf<T extends IVoluntario | NewVoluntario> = Omit<T, 'fechaNacimiento'> & {
  fechaNacimiento?: string | null;
};

export type RestVoluntario = RestOf<IVoluntario>;

export type NewRestVoluntario = RestOf<NewVoluntario>;

export type PartialUpdateRestVoluntario = RestOf<PartialUpdateVoluntario>;

export type EntityResponseType = HttpResponse<IVoluntario>;
export type EntityArrayResponseType = HttpResponse<IVoluntario[]>;

@Injectable({ providedIn: 'root' })
export class VoluntarioService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/voluntarios');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(voluntario: NewVoluntario): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(voluntario);
    return this.http
      .post<RestVoluntario>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(voluntario: IVoluntario): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(voluntario);
    return this.http
      .put<RestVoluntario>(`${this.resourceUrl}/${this.getVoluntarioIdentifier(voluntario)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(voluntario: PartialUpdateVoluntario): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(voluntario);
    return this.http
      .patch<RestVoluntario>(`${this.resourceUrl}/${this.getVoluntarioIdentifier(voluntario)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestVoluntario>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestVoluntario[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getVoluntarioIdentifier(voluntario: Pick<IVoluntario, 'id'>): number {
    return voluntario.id;
  }

  compareVoluntario(o1: Pick<IVoluntario, 'id'> | null, o2: Pick<IVoluntario, 'id'> | null): boolean {
    return o1 && o2 ? this.getVoluntarioIdentifier(o1) === this.getVoluntarioIdentifier(o2) : o1 === o2;
  }

  addVoluntarioToCollectionIfMissing<Type extends Pick<IVoluntario, 'id'>>(
    voluntarioCollection: Type[],
    ...voluntariosToCheck: (Type | null | undefined)[]
  ): Type[] {
    const voluntarios: Type[] = voluntariosToCheck.filter(isPresent);
    if (voluntarios.length > 0) {
      const voluntarioCollectionIdentifiers = voluntarioCollection.map(voluntarioItem => this.getVoluntarioIdentifier(voluntarioItem)!);
      const voluntariosToAdd = voluntarios.filter(voluntarioItem => {
        const voluntarioIdentifier = this.getVoluntarioIdentifier(voluntarioItem);
        if (voluntarioCollectionIdentifiers.includes(voluntarioIdentifier)) {
          return false;
        }
        voluntarioCollectionIdentifiers.push(voluntarioIdentifier);
        return true;
      });
      return [...voluntariosToAdd, ...voluntarioCollection];
    }
    return voluntarioCollection;
  }

  protected convertDateFromClient<T extends IVoluntario | NewVoluntario | PartialUpdateVoluntario>(voluntario: T): RestOf<T> {
    return {
      ...voluntario,
      fechaNacimiento: voluntario.fechaNacimiento?.format(DATE_FORMAT) ?? null,
    };
  }

  protected convertDateFromServer(restVoluntario: RestVoluntario): IVoluntario {
    return {
      ...restVoluntario,
      fechaNacimiento: restVoluntario.fechaNacimiento ? dayjs(restVoluntario.fechaNacimiento) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestVoluntario>): HttpResponse<IVoluntario> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestVoluntario[]>): HttpResponse<IVoluntario[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
