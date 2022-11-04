import { EquipoService } from './../../equipo/service/equipo.service';
import { PuntoControlService } from './../../punto-control/service/punto-control.service';
import { IPuntoControl } from './../../punto-control/punto-control.model';
import { IEquipo } from './../../equipo/equipo.model';
import { Component, OnInit } from '@angular/core';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { ActivatedRoute, Data, ParamMap, Router } from '@angular/router';
import { combineLatest, filter, map, Observable, switchMap, tap } from 'rxjs';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IPasoControl } from '../paso-control.model';

import { ITEMS_PER_PAGE, PAGE_HEADER, TOTAL_COUNT_RESPONSE_HEADER } from 'app/config/pagination.constants';
import { ASC, DESC, SORT, ITEM_DELETED_EVENT, DEFAULT_SORT_DATA } from 'app/config/navigation.constants';
import { EntityArrayResponseType, PasoControlService } from '../service/paso-control.service';
import { PasoControlDeleteDialogComponent } from '../delete/paso-control-delete-dialog.component';
import { FilterOptions, IFilterOptions, IFilterOption } from 'app/shared/filter/filter.model';

@Component({
  selector: 'jhi-paso-control',
  templateUrl: './paso-control.component.html',
  styleUrls: ['./paso-control.component.scss']
})
export class PasoControlComponent implements OnInit {
  pasoControls?: IPasoControl[];
  isLoading = false;

  predicate = 'id';
  ascending = true;
  filters: IFilterOptions = new FilterOptions();

  // Búsqueda avanzada
  activeAdvancedSearch = false;
  equipoFilter?: IEquipo;
  equiposSharedCollection: IEquipo[] = [];
  puntoControlFilter?: IPuntoControl;
  puntoControlsSharedCollection: IPuntoControl[] = [];


  itemsPerPage = ITEMS_PER_PAGE;
  totalItems = 0;
  page = 1;

  constructor(
    protected pasoControlService: PasoControlService,
    protected activatedRoute: ActivatedRoute,
    protected puntoControlService: PuntoControlService,
    protected equipoService: EquipoService,
    public router: Router,
    protected modalService: NgbModal
  ) {}

  trackId = (_index: number, item: IPasoControl): number => this.pasoControlService.getPasoControlIdentifier(item);

  ngOnInit(): void {
    this.load();

    this.filters.filterChanges.subscribe(filterOptions => this.handleNavigation(1, this.predicate, this.ascending, filterOptions));
  }

  delete(pasoControl: IPasoControl): void {
    const modalRef = this.modalService.open(PasoControlDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.pasoControl = pasoControl;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed
      .pipe(
        filter(reason => reason === ITEM_DELETED_EVENT),
        switchMap(() => this.loadFromBackendWithRouteInformations())
      )
      .subscribe({
        next: (res: EntityArrayResponseType) => {
          this.onResponseSuccess(res);
        },
      });
  }

  load(): void {
    this.loadFromBackendWithRouteInformations().subscribe({
      next: (res: EntityArrayResponseType) => {
        this.onResponseSuccess(res);
      },
    });
  }

  navigateToWithComponentValues(): void {
    this.handleNavigation(this.page, this.predicate, this.ascending, this.filters.filterOptions);
  }

  navigateToPage(page = this.page): void {
    this.handleNavigation(page, this.predicate, this.ascending, this.filters.filterOptions);
  }

  searchEquipo(event: any): void {
    const IdentificadorNombreEquipo = event.query;

    const queryIdentificadorNombre: any = {
      sort:['nombre,asc']
    };

    if(IdentificadorNombreEquipo){
      queryIdentificadorNombre["identificador.contains"] = IdentificadorNombreEquipo;
      queryIdentificadorNombre["nombre.contains"] = IdentificadorNombreEquipo;
    }

    this.equipoService.query(queryIdentificadorNombre)
    .pipe(map((res: HttpResponse<IEquipo[]>) => res.body ?? []))
    .subscribe((equipos: IEquipo[]) => (this.equiposSharedCollection = equipos));
  }

  searchPuntoControl(event: any): void {
    const nombrePuntoControl = event.query;

    const queryPControlObject: any = {
      sort:['nombre,asc']
    };

    if(nombrePuntoControl){
      queryPControlObject["nombre.contains"] = nombrePuntoControl;
    }

    this.puntoControlService.query(queryPControlObject)
    .pipe(map((res: HttpResponse<IPuntoControl[]>) => res.body ?? []))
    .subscribe((puntos: IPuntoControl[]) => (this.puntoControlsSharedCollection = puntos));
  }

  advancedSearch(): void {
    if(this.equipoFilter){
      const equipoFilterOption = this.filters.getFilterOptionByName('equipoId.in');

      if(equipoFilterOption){
        this.filters.removeFilter('equipoId.in');
      }

      this.filters.addFilter('equipoId.in', ...[this.equipoFilter.id.toString()]);
    }

    if(this.puntoControlFilter){
      const puntoControlFilterOption = this.filters.getFilterOptionByName('puntoControlId.in');

      if(puntoControlFilterOption){
        this.filters.removeFilter('puntoControlId.in');
      }

      this.filters.addFilter('puntoControlId.in', ...[this.puntoControlFilter.id.toString()]);
    }

    // Si todos los campos están vacíos y el usuario da a buscar, se hace un clear.
    if(!this.equipoFilter && !this.puntoControlFilter){
      this.clearAdvancedSearch();
    }
  }

  clearAdvancedSearch(): void {
    this.filters.clear();
    this.equipoFilter = undefined;
    this.puntoControlFilter = undefined;
  }

  protected loadFromBackendWithRouteInformations(): Observable<EntityArrayResponseType> {
    return combineLatest([this.activatedRoute.queryParamMap, this.activatedRoute.data]).pipe(
      tap(([params, data]) => this.fillComponentAttributeFromRoute(params, data)),
      switchMap(() => this.queryBackend(this.page, this.predicate, this.ascending, this.filters.filterOptions))
    );
  }

  protected fillComponentAttributeFromRoute(params: ParamMap, data: Data): void {
    const page = params.get(PAGE_HEADER);
    this.page = +(page ?? 1);
    const sort = (params.get(SORT) ?? data[DEFAULT_SORT_DATA]).split(',');
    this.predicate = sort[0];
    this.ascending = sort[1] === ASC;
    this.filters.initializeFromParams(params);
  }

  protected onResponseSuccess(response: EntityArrayResponseType): void {
    this.fillComponentAttributesFromResponseHeader(response.headers);
    const dataFromBody = this.fillComponentAttributesFromResponseBody(response.body);
    this.pasoControls = dataFromBody;
  }

  protected fillComponentAttributesFromResponseBody(data: IPasoControl[] | null): IPasoControl[] {
    return data ?? [];
  }

  protected fillComponentAttributesFromResponseHeader(headers: HttpHeaders): void {
    this.totalItems = Number(headers.get(TOTAL_COUNT_RESPONSE_HEADER));
  }

  protected queryBackend(
    page?: number,
    predicate?: string,
    ascending?: boolean,
    filterOptions?: IFilterOption[]
  ): Observable<EntityArrayResponseType> {
    this.isLoading = true;
    const pageToLoad: number = page ?? 1;
    const queryObject: any = {
      page: pageToLoad - 1,
      size: this.itemsPerPage,
      sort: this.getSortQueryParam(predicate, ascending),
    };
    filterOptions?.forEach(filterOption => {
      queryObject[filterOption.name] = filterOption.values;
    });
    return this.pasoControlService.query(queryObject).pipe(tap(() => (this.isLoading = false)));
  }

  protected handleNavigation(page = this.page, predicate?: string, ascending?: boolean, filterOptions?: IFilterOption[]): void {
    const queryParamsObj: any = {
      page,
      size: this.itemsPerPage,
      sort: this.getSortQueryParam(predicate, ascending),
    };

    filterOptions?.forEach(filterOption => {
      queryParamsObj[filterOption.nameAsQueryParam()] = filterOption.values;
    });

    this.router.navigate(['./'], {
      relativeTo: this.activatedRoute,
      queryParams: queryParamsObj,
    });
  }

  protected getSortQueryParam(predicate = this.predicate, ascending = this.ascending): string[] {
    const ascendingQueryParam = ascending ? ASC : DESC;
    if (predicate === '') {
      return [];
    } else {
      return [predicate + ',' + ascendingQueryParam];
    }
  }
}
