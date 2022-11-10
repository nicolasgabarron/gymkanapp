import { PasoControlService } from './../service/paso-control.service';
import { IPasoControl } from './../paso-control.model';
import { PuntoControlService } from './../../punto-control/service/punto-control.service';
import { EquipoService } from './../../equipo/service/equipo.service';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { IPuntoControl } from './../../punto-control/punto-control.model';
import { IEquipo } from './../../equipo/equipo.model';
import { Component, OnInit } from '@angular/core';
import { map } from 'rxjs';
import { HttpResponse } from '@angular/common/http';
import autoTable from 'jspdf-autotable'
import jsPDF from 'jspdf';

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
    protected activeModal: NgbActiveModal,
    protected equipoService: EquipoService,
    protected puntoControlService: PuntoControlService,
    protected pasoControlService: PasoControlService
  ) { }

  ngOnInit(): void {
  }

  // Funciones
  searchEquipos(event: any): void{
    const IdentificadorNombreEquipo = event.query;

    const queryIdentificadorNombre: any = {
      sort:['nombre,asc']
    };

    if(IdentificadorNombreEquipo){
      queryIdentificadorNombre["identificador.contains"] = IdentificadorNombreEquipo;
      // queryIdentificadorNombre["nombre.contains"] = IdentificadorNombreEquipo;
    }

    this.equipoService.query(queryIdentificadorNombre)
    .pipe(map((res: HttpResponse<IEquipo[]>) => res.body ?? []))
    .subscribe((equipos: IEquipo[]) => (this.equiposSharedCollection = equipos));
  }

  searchPuntosControl(event: any): void {
    const nombrePuntoControl = event.query;

    const queryPControlObject: any = {
      sort:['nombre,asc']
    };

    if(nombrePuntoControl){
      queryPControlObject["nombre.contains"] = nombrePuntoControl;
    }

    this.puntoControlService.query(queryPControlObject)
    .pipe(map((res: HttpResponse<IPuntoControl[]>) => res.body ?? []))
    .subscribe((puntos: IPuntoControl[]) => (this.puntosControlSharedCollection = puntos));
  }

  cancel(): void{
    this.activeModal.close();
  }

  download(): void{
    // Lista a través de la que crearé la tabla
    let controlPasoDownload: IPasoControl[] = [];

    // Recupero los datos
    this.queryBackend().then(controlPasos => {
      controlPasoDownload = controlPasos;

      // Columnas a mostrar.
      const columnas: string[] = ['Fecha y hora', 'Equipo', 'Punto de control', 'Validado por'];

      const registros: any = [];

      controlPasoDownload.forEach(controlPaso => {
        registros.push([controlPaso.fechaHora?.format('DD/MM/YYYY HH:mm:ss'), controlPaso.equipo?.identificador, controlPaso.puntoControl?.nombre, controlPaso.validadoPor?.dni])
      });

      const doc = new jsPDF();

      if(this.equipoForm && this.puntoControlForm){
        doc.text(`Registros del equipo '${this.equipoForm.identificador!.toString()}' en el puesto de control '${this.puntoControlForm.nombre!}'`, 20, 20, {maxWidth: 150});
      } else if (this.equipoForm){
        doc.text(`Registros del equipo '${this.equipoForm.identificador!.toString()}'`, 20, 20);
      } else if (this.puntoControlForm) {
        doc.text(`Registros del punto de control '${this.puntoControlForm.nombre!}'`, 20, 20);
      }

      autoTable(doc, {
        theme: "grid",
        head: [columnas],
        body: registros,
        startY: this.equipoForm && this.puntoControlForm ? 30 : 25
      });

      if(this.equipoForm && this.puntoControlForm){
        doc.save(`${this.equipoForm.identificador}_${this.puntoControlForm.nombre}`);
      } else if (this.equipoForm){
        doc.save(`${this.equipoForm.identificador}`);
      } else if (this.puntoControlForm) {
        doc.save(`${this.puntoControlForm.nombre}`);
      }

      // Prueba
    })

  }

  async queryBackend(): Promise<any> {
    const queryPasoObject: any = {
      sort: ['fechaHora,asc']
    };

    if(this.equipoForm){
      queryPasoObject['equipoId.in'] = this.equipoForm.id.toString();
    }

    if(this.puntoControlForm) {
      queryPasoObject['puntoControlId.in'] = this.puntoControlForm.id.toString();
    }

    return await this.pasoControlService.query(queryPasoObject)
    .pipe(map((res:HttpResponse<IPasoControl[]>) => res.body ?? []))
    .toPromise();
  }

}
