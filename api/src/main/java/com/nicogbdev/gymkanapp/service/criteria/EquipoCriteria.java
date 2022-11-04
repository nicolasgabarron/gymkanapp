package com.nicogbdev.gymkanapp.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import org.springdoc.api.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.nicogbdev.gymkanapp.domain.Equipo} entity. This class is used
 * in {@link com.nicogbdev.gymkanapp.web.rest.EquipoResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /equipos?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class EquipoCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter identificador;

    private StringFilter nombre;

    private LongFilter participantesId;

    private Boolean distinct;

    public EquipoCriteria() {}

    public EquipoCriteria(EquipoCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.identificador = other.identificador == null ? null : other.identificador.copy();
        this.nombre = other.nombre == null ? null : other.nombre.copy();
        this.participantesId = other.participantesId == null ? null : other.participantesId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public EquipoCriteria copy() {
        return new EquipoCriteria(this);
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

    public LongFilter getParticipantesId() {
        return participantesId;
    }

    public LongFilter participantesId() {
        if (participantesId == null) {
            participantesId = new LongFilter();
        }
        return participantesId;
    }

    public void setParticipantesId(LongFilter participantesId) {
        this.participantesId = participantesId;
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
        final EquipoCriteria that = (EquipoCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(identificador, that.identificador) &&
            Objects.equals(nombre, that.nombre) &&
            Objects.equals(participantesId, that.participantesId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, identificador, nombre, participantesId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "EquipoCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (identificador != null ? "identificador=" + identificador + ", " : "") +
            (nombre != null ? "nombre=" + nombre + ", " : "") +
            (participantesId != null ? "participantesId=" + participantesId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
