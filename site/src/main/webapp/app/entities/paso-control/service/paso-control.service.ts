import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IPasoControl, NewPasoControl } from '../paso-control.model';

export type PartialUpdatePasoControl = Partial<IPasoControl> & Pick<IPasoControl, 'id'>;

type RestOf<T extends IPasoControl | NewPasoControl> = Omit<T, 'fechaHora'> & {
  fechaHora?: string | null;
};

export type RestPasoControl = RestOf<IPasoControl>;

export type NewRestPasoControl = RestOf<NewPasoControl>;

export type PartialUpdateRestPasoControl = RestOf<PartialUpdatePasoControl>;

export type EntityResponseType = HttpResponse<IPasoControl>;
export type EntityArrayResponseType = HttpResponse<IPasoControl[]>;

@Injectable({ providedIn: 'root' })
export class PasoControlService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/paso-controls');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(pasoControl: NewPasoControl): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(pasoControl);
    return this.http
      .post<RestPasoControl>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(pasoControl: IPasoControl): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(pasoControl);
    return this.http
      .put<RestPasoControl>(`${this.resourceUrl}/${this.getPasoControlIdentifier(pasoControl)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(pasoControl: PartialUpdatePasoControl): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(pasoControl);
    return this.http
      .patch<RestPasoControl>(`${this.resourceUrl}/${this.getPasoControlIdentifier(pasoControl)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestPasoControl>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestPasoControl[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getPasoControlIdentifier(pasoControl: Pick<IPasoControl, 'id'>): number {
    return pasoControl.id;
  }

  comparePasoControl(o1: Pick<IPasoControl, 'id'> | null, o2: Pick<IPasoControl, 'id'> | null): boolean {
    return o1 && o2 ? this.getPasoControlIdentifier(o1) === this.getPasoControlIdentifier(o2) : o1 === o2;
  }

  addPasoControlToCollectionIfMissing<Type extends Pick<IPasoControl, 'id'>>(
    pasoControlCollection: Type[],
    ...pasoControlsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const pasoControls: Type[] = pasoControlsToCheck.filter(isPresent);
    if (pasoControls.length > 0) {
      const pasoControlCollectionIdentifiers = pasoControlCollection.map(
        pasoControlItem => this.getPasoControlIdentifier(pasoControlItem)!
      );
      const pasoControlsToAdd = pasoControls.filter(pasoControlItem => {
        const pasoControlIdentifier = this.getPasoControlIdentifier(pasoControlItem);
        if (pasoControlCollectionIdentifiers.includes(pasoControlIdentifier)) {
          return false;
        }
        pasoControlCollectionIdentifiers.push(pasoControlIdentifier);
        return true;
      });
      return [...pasoControlsToAdd, ...pasoControlCollection];
    }
    return pasoControlCollection;
  }

  protected convertDateFromClient<T extends IPasoControl | NewPasoControl | PartialUpdatePasoControl>(pasoControl: T): RestOf<T> {
    return {
      ...pasoControl,
      fechaHora: pasoControl.fechaHora?.toJSON() ?? null,
    };
  }

  protected convertDateFromServer(restPasoControl: RestPasoControl): IPasoControl {
    return {
      ...restPasoControl,
      fechaHora: restPasoControl.fechaHora ? dayjs(restPasoControl.fechaHora) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestPasoControl>): HttpResponse<IPasoControl> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestPasoControl[]>): HttpResponse<IPasoControl[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
