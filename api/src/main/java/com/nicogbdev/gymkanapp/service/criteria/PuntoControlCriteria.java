package com.nicogbdev.gymkanapp.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import org.springdoc.api.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.nicogbdev.gymkanapp.domain.PuntoControl} entity. This class is used
 * in {@link com.nicogbdev.gymkanapp.web.rest.PuntoControlResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /punto-controls?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class PuntoControlCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter identificador;

    private IntegerFilter orden;

    private StringFilter nombre;

    private StringFilter direccion;

    private LongFilter voluntariosId;

    private Boolean distinct;

    public PuntoControlCriteria() {}

    public PuntoControlCriteria(PuntoControlCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.identificador = other.identificador == null ? null : other.identificador.copy();
        this.orden = other.orden == null ? null : other.orden.copy();
        this.nombre = other.nombre == null ? null : other.nombre.copy();
        this.direccion = other.direccion == null ? null : other.direccion.copy();
        this.voluntariosId = other.voluntariosId == null ? null : other.voluntariosId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public PuntoControlCriteria copy() {
        return new PuntoControlCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public LongFilter id() {
        if (id == null) {
            id = new LongFilter();
        }
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getIdentificador() {
        return identificador;
    }

    public StringFilter identificador() {
        if (identificador == null) {
            identificador = new StringFilter();
        }
        return identificador;
    }

    public void setIdentificador(StringFilter identificador) {
        this.identificador = identificador;
    }

    public IntegerFilter getOrden() {
        return orden;
    }

    public IntegerFilter orden() {
        if (orden == null) {
            orden = new IntegerFilter();
        }
        return orden;
    }

    public void setOrden(IntegerFilter orden) {
        this.orden = orden;
    }

    public StringFilter getNombre() {
        return nombre;
    }

    public StringFilter nombre() {
        if (nombre == null) {
            nombre = new StringFilter();
        }
        return nombre;
    }

    public void setNombre(StringFilter nombre) {
        this.nombre = nombre;
    }

    public StringFilter getDireccion() {
        return direccion;
    }

    public StringFilter direccion() {
        if (direccion == null) {
            direccion = new StringFilter();
        }
        return direccion;
    }

    public void setDireccion(StringFilter direccion) {
        this.direccion = direccion;
    }

    public LongFilter getVoluntariosId() {
        return voluntariosId;
    }

    public LongFilter voluntariosId() {
        if (voluntariosId == null) {
            voluntariosId = new LongFilter();
        }
        return voluntariosId;
    }

    public void setVoluntariosId(LongFilter voluntariosId) {
        this.voluntariosId = voluntariosId;
    }

    public Boolean getDistinct() {
        return distinct;
    }

    public void setDistinct(Boolean distinct) {
        this.distinct = distinct;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final PuntoControlCriteria that = (PuntoControlCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(identificador, that.identificador) &&
            Objects.equals(orden, that.orden) &&
            Objects.equals(nombre, that.nombre) &&
            Objects.equals(direccion, that.direccion) &&
            Objects.equals(voluntariosId, that.voluntariosId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, identificador, orden, nombre, direccion, voluntariosId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PuntoControlCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (identificador != null ? "identificador=" + identificador + ", " : "") +
            (orden != null ? "orden=" + orden + ", " : "") +
            (nombre != null ? "nombre=" + nombre + ", " : "") +
            (direccion != null ? "direccion=" + direccion + ", " : "") +
            (voluntariosId != null ? "voluntariosId=" + voluntariosId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
