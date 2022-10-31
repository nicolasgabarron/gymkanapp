import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IPuntoControl } from '../punto-control.model';
import { PuntoControlService } from '../service/punto-control.service';

@Injectable({ providedIn: 'root' })
export class PuntoControlRoutingResolveService implements Resolve<IPuntoControl | null> {
  constructor(protected service: PuntoControlService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IPuntoControl | null | never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((puntoControl: HttpResponse<IPuntoControl>) => {
          if (puntoControl.body) {
            return of(puntoControl.body);
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
