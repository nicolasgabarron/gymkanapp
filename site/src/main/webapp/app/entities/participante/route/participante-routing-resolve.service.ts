import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IParticipante } from '../participante.model';
import { ParticipanteService } from '../service/participante.service';

@Injectable({ providedIn: 'root' })
export class ParticipanteRoutingResolveService implements Resolve<IParticipante | null> {
  constructor(protected service: ParticipanteService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IParticipante | null | never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((participante: HttpResponse<IParticipante>) => {
          if (participante.body) {
            return of(participante.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(null);
  }
}
