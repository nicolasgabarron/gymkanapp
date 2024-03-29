import { EquipoService } from './../../equipo/service/equipo.service';
import { IEquipo } from './../../equipo/equipo.model';
import { Component, OnInit } from '@angular/core';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { ActivatedRoute, Data, ParamMap, Router } from '@angular/router';
import { combineLatest, filter, map, Observable, switchMap, tap } from 'rxjs';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IParticipante } from '../participante.model';

import { ITEMS_PER_PAGE, PAGE_HEADER, TOTAL_COUNT_RESPONSE_HEADER } from 'app/config/pagination.constants';
import { ASC, DESC, SORT, ITEM_DELETED_EVENT, DEFAULT_SORT_DATA } from 'app/config/navigation.constants';
import { EntityArrayResponseType, ParticipanteService } from '../service/participante.service';
import { ParticipanteDeleteDialogComponent } from '../delete/participante-delete-dialog.component';
import { FilterOptions, IFilterOptions, IFilterOption } from 'app/shared/filter/filter.model';

@Component({
  selector: 'jhi-participante',
  templateUrl: './participante.component.html',
  styleUrls: ['./participante.component.scss']
})
export class ParticipanteComponent implements OnInit {
  participantes?: IParticipante[];
  isLoading = false;

  predicate = 'id';
  ascending = true;
  filters: IFilterOptions = new FilterOptions();

  // Búsqueda avanzada
  activeAdvancedSearch = false;
  dniFilter?: string;
  nombreFilter?: string;
  apellidosFilter?: string;
  equipoFilter?: IEquipo;
  equiposSharedCollection: IEquipo[] = [];


  itemsPerPage = ITEMS_PER_PAGE;
  totalItems = 0;
  page = 1;

  constructor(
    protected participanteService: ParticipanteService,
    protected activatedRoute: ActivatedRoute,
    protected equipoService: EquipoService,
    public router: Router,
    protected modalService: NgbModal
  ) {}

  trackId = (_index: number, item: IParticipante): number => this.participanteService.getParticipanteIdentifier(item);

  ngOnInit(): void {
    this.load();

    this.filters.filterChanges.subscribe(filterOptions => this.handleNavigation(1, this.predicate, this.ascending, filterOptions));
  }

  delete(participante: IParticipante): void {
    const modalRef = this.modalService.open(ParticipanteDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.participante = participante;
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

  searchEquipo(event: any): void{
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

  advancedSearch(): void{
    // Comprobaciones de qué datos están cambiados
    if(this.dniFilter){
      const dniFilterOption = this.filters.getFilterOptionByName('dni.contains');

      if(dniFilterOption){
        this.filters.removeFilter('dni.contains');
      }

      this.filters.addFilter('dni.contains', ...[this.dniFilter]);
    }

    if(this.nombreFilter){
      const nombreFilterOption = this.filters.getFilterOptionByName('nombre.contains');

      if(nombreFilterOption){
        this.filters.removeFilter('nombre.contains');
      }

      this.filters.addFilter('nombre.contains', ...[this.nombreFilter]);
    }

    if(this.apellidosFilter){
      const apellidosFilterOption = this.filters.getFilterOptionByName('apellidos.contains');

      if(apellidosFilterOption){
        this.filters.removeFilter('apellidos.contains');
      }

      this.filters.addFilter('apellidos.contains', ...[this.apellidosFilter]);
    }

    if(this.equipoFilter){
      const equipoFilterOption = this.filters.getFilterOptionByName('equipoId.in');

      if(equipoFilterOption){
        this.filters.removeFilter('equipoId.in');
      }

      this.filters.addFilter('equipoId.in', ...[this.equipoFilter.id.toString()]);
    }

    // Si todos los campos están vacíos y el usuario da a buscar, se hace un clear.
    if(!this.dniFilter && !this.nombreFilter && !this.apellidosFilter && !this.equipoFilter){
      this.clearAdvancedSearch();
    }
  }

  clearAdvancedSearch(): void{
    this.filters.clear();
    this.dniFilter = '';
    this.nombreFilter = '';
    this.apellidosFilter = '';
    this.equipoFilter = undefined;
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
    this.participantes = dataFromBody;
  }

  protected fillComponentAttributesFromResponseBody(data: IParticipante[] | null): IParticipante[] {
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
    return this.participanteService.query(queryObject).pipe(tap(() => (this.isLoading = false)));
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
