import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IVoluntario } from '../voluntario.model';
import { VoluntarioService } from '../service/voluntario.service';

@Injectable({ providedIn: 'root' })
export class VoluntarioRoutingResolveService implements Resolve<IVoluntario | null> {
  constructor(protected service: VoluntarioService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IVoluntario | null | never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((voluntario: HttpResponse<IVoluntario>) => {
          if (voluntario.body) {
            return of(voluntario.body);
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
