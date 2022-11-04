import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { IPuntoControl } from './../../punto-control/punto-control.model';
import { IEquipo } from './../../equipo/equipo.model';
import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'jhi-download-pdf',
  templateUrl: './download-pdf.component.html',
  styleUrls: ['./download-pdf.component.scss']
})
export class DownloadPdfComponent implements OnInit {

  // Propiedades
  equipoForm?: IEquipo;
  equiposSharedCollection: IEquipo[] = [];
  puntoControlForm?: IPuntoControl;
  puntosControlSharedCollection: IPuntoControl[] = [];

  constructor(
    protected activeModal: NgbActiveModal
  ) { }

  ngOnInit(): void {
  }

  // Funciones
  searchEquipos(event: any): void{
    //
  }

  searchPuntosControl(event: any): void {
    //
  }

  cancel(): void{
    this.activeModal.close();
  }

  download(): void{
    //
  }

}
