import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IEquipo, NewEquipo } from '../equipo.model';

export type PartialUpdateEquipo = Partial<IEquipo> & Pick<IEquipo, 'id'>;

export type EntityResponseType = HttpResponse<IEquipo>;
export type EntityArrayResponseType = HttpResponse<IEquipo[]>;

@Injectable({ providedIn: 'root' })
export class EquipoService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/equipos');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(equipo: NewEquipo): Observable<EntityResponseType> {
    return this.http.post<IEquipo>(this.resourceUrl, equipo, { observe: 'response' });
  }

  update(equipo: IEquipo): Observable<EntityResponseType> {
    return this.http.put<IEquipo>(`${this.resourceUrl}/${this.getEquipoIdentifier(equipo)}`, equipo, { observe: 'response' });
  }

  partialUpdate(equipo: PartialUpdateEquipo): Observable<EntityResponseType> {
    return this.http.patch<IEquipo>(`${this.resourceUrl}/${this.getEquipoIdentifier(equipo)}`, equipo, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IEquipo>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IEquipo[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getEquipoIdentifier(equipo: Pick<IEquipo, 'id'>): number {
    return equipo.id;
  }

  compareEquipo(o1: Pick<IEquipo, 'id'> | null, o2: Pick<IEquipo, 'id'> | null): boolean {
    return o1 && o2 ? this.getEquipoIdentifier(o1) === this.getEquipoIdentifier(o2) : o1 === o2;
  }

  addEquipoToCollectionIfMissing<Type extends Pick<IEquipo, 'id'>>(
    equipoCollection: Type[],
    ...equiposToCheck: (Type | null | undefined)[]
  ): Type[] {
    const equipos: Type[] = equiposToCheck.filter(isPresent);
    if (equipos.length > 0) {
      const equipoCollectionIdentifiers = equipoCollection.map(equipoItem => this.getEquipoIdentifier(equipoItem)!);
      const equiposToAdd = equipos.filter(equipoItem => {
        const equipoIdentifier = this.getEquipoIdentifier(equipoItem);
        if (equipoCollectionIdentifiers.includes(equipoIdentifier)) {
          return false;
        }
        equipoCollectionIdentifiers.push(equipoIdentifier);
        return true;
      });
      return [...equiposToAdd, ...equipoCollection];
    }
    return equipoCollection;
  }
}
