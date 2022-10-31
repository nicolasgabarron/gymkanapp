import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IPasoControl } from '../paso-control.model';
import { PasoControlService } from '../service/paso-control.service';

@Injectable({ providedIn: 'root' })
export class PasoControlRoutingResolveService implements Resolve<IPasoControl | null> {
  constructor(protected service: PasoControlService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IPasoControl | null | never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((pasoControl: HttpResponse<IPasoControl>) => {
          if (pasoControl.body) {
            return of(pasoControl.body);
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
