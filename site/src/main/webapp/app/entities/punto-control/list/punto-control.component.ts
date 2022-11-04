import { Component, OnInit } from '@angular/core';
import { HttpHeaders } from '@angular/common/http';
import { ActivatedRoute, Data, ParamMap, Router } from '@angular/router';
import { combineLatest, filter, Observable, switchMap, tap } from 'rxjs';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IPuntoControl } from '../punto-control.model';

import { ITEMS_PER_PAGE, PAGE_HEADER, TOTAL_COUNT_RESPONSE_HEADER } from 'app/config/pagination.constants';
import { ASC, DESC, SORT, ITEM_DELETED_EVENT, DEFAULT_SORT_DATA } from 'app/config/navigation.constants';
import { EntityArrayResponseType, PuntoControlService } from '../service/punto-control.service';
import { PuntoControlDeleteDialogComponent } from '../delete/punto-control-delete-dialog.component';
import { FilterOptions, IFilterOptions, IFilterOption } from 'app/shared/filter/filter.model';

@Component({
  selector: 'jhi-punto-control',
  templateUrl: './punto-control.component.html',
  styleUrls: ['./punto-control.component.scss']
})
export class PuntoControlComponent implements OnInit {
  puntoControls?: IPuntoControl[];
  isLoading = false;

  predicate = 'id';
  ascending = true;
  filters: IFilterOptions = new FilterOptions();

  // Búsqueda avanzada
  activeAdvancedSearch = false;
  identificadorFilter?: string;
  nombreFilter?: string;
  direccionFilter?: string;

  itemsPerPage = ITEMS_PER_PAGE;
  totalItems = 0;
  page = 1;

  constructor(
    protected puntoControlService: PuntoControlService,
    protected activatedRoute: ActivatedRoute,
    public router: Router,
    protected modalService: NgbModal
  ) { }

  trackId = (_index: number, item: IPuntoControl): number => this.puntoControlService.getPuntoControlIdentifier(item);

  ngOnInit(): void {
    this.load();

    this.filters.filterChanges.subscribe(filterOptions => this.handleNavigation(1, this.predicate, this.ascending, filterOptions));
  }

  delete(puntoControl: IPuntoControl): void {
    const modalRef = this.modalService.open(PuntoControlDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.puntoControl = puntoControl;
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

  advancedSearch(): void {
    // Comprobaciones de qué datos están cambiados
    if (this.identificadorFilter) {
      const identificadorFilterOption = this.filters.getFilterOptionByName('identificador.contains');

      if (identificadorFilterOption) {
        this.filters.removeFilter('identificador.contains');
      }

      this.filters.addFilter('identificador.contains', ...[this.identificadorFilter]);
    }

    if (this.nombreFilter) {
      const nombreFilterOption = this.filters.getFilterOptionByName('nombre.contains');

      if (nombreFilterOption) {
        this.filters.removeFilter('nombre.contains');
      }

      this.filters.addFilter('nombre.contains', ...[this.nombreFilter]);
    }

    if (this.direccionFilter) {
      const direccionFilterOption = this.filters.getFilterOptionByName('direccion.contains');

      if (direccionFilterOption) {
        this.filters.removeFilter('direccion.contains');
      }

      this.filters.addFilter('direccion.contains', ...[this.direccionFilter]);
    }

    // Si todos los campos están vacíos y el usuario da a buscar, se hace un clear.
    if (!this.identificadorFilter && !this.nombreFilter && !this.direccionFilter) {
      this.clearAdvancedSearch();
    }
  }

  clearAdvancedSearch(): void {
    this.filters.clear();
    this.identificadorFilter = '';
    this.nombreFilter = '';
    this.direccionFilter = '';
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
    this.puntoControls = dataFromBody;
  }

  protected fillComponentAttributesFromResponseBody(data: IPuntoControl[] | null): IPuntoControl[] {
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
    return this.puntoControlService.query(queryObject).pipe(tap(() => (this.isLoading = false)));
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
