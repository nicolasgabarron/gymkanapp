import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IEquipo } from '../equipo.model';
import { EquipoService } from '../service/equipo.service';

@Injectable({ providedIn: 'root' })
export class EquipoRoutingResolveService implements Resolve<IEquipo | null> {
  constructor(protected service: EquipoService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IEquipo | null | never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((equipo: HttpResponse<IEquipo>) => {
          if (equipo.body) {
            return of(equipo.body);
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
