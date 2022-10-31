import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IPuntoControl, NewPuntoControl } from '../punto-control.model';

export type PartialUpdatePuntoControl = Partial<IPuntoControl> & Pick<IPuntoControl, 'id'>;

export type EntityResponseType = HttpResponse<IPuntoControl>;
export type EntityArrayResponseType = HttpResponse<IPuntoControl[]>;

@Injectable({ providedIn: 'root' })
export class PuntoControlService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/punto-controls');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(puntoControl: NewPuntoControl): Observable<EntityResponseType> {
    return this.http.post<IPuntoControl>(this.resourceUrl, puntoControl, { observe: 'response' });
  }

  update(puntoControl: IPuntoControl): Observable<EntityResponseType> {
    return this.http.put<IPuntoControl>(`${this.resourceUrl}/${this.getPuntoControlIdentifier(puntoControl)}`, puntoControl, {
      observe: 'response',
    });
  }

  partialUpdate(puntoControl: PartialUpdatePuntoControl): Observable<EntityResponseType> {
    return this.http.patch<IPuntoControl>(`${this.resourceUrl}/${this.getPuntoControlIdentifier(puntoControl)}`, puntoControl, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IPuntoControl>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IPuntoControl[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getPuntoControlIdentifier(puntoControl: Pick<IPuntoControl, 'id'>): number {
    return puntoControl.id;
  }

  comparePuntoControl(o1: Pick<IPuntoControl, 'id'> | null, o2: Pick<IPuntoControl, 'id'> | null): boolean {
    return o1 && o2 ? this.getPuntoControlIdentifier(o1) === this.getPuntoControlIdentifier(o2) : o1 === o2;
  }

  addPuntoControlToCollectionIfMissing<Type extends Pick<IPuntoControl, 'id'>>(
    puntoControlCollection: Type[],
    ...puntoControlsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const puntoControls: Type[] = puntoControlsToCheck.filter(isPresent);
    if (puntoControls.length > 0) {
      const puntoControlCollectionIdentifiers = puntoControlCollection.map(
        puntoControlItem => this.getPuntoControlIdentifier(puntoControlItem)!
      );
      const puntoControlsToAdd = puntoControls.filter(puntoControlItem => {
        const puntoControlIdentifier = this.getPuntoControlIdentifier(puntoControlItem);
        if (puntoControlCollectionIdentifiers.includes(puntoControlIdentifier)) {
          return false;
        }
        puntoControlCollectionIdentifiers.push(puntoControlIdentifier);
        return true;
      });
      return [...puntoControlsToAdd, ...puntoControlCollection];
    }
    return puntoControlCollection;
  }
}
