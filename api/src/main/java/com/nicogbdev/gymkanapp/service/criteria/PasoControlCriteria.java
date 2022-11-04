package com.nicogbdev.gymkanapp.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import org.springdoc.api.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.nicogbdev.gymkanapp.domain.PasoControl} entity. This class is used
 * in {@link com.nicogbdev.gymkanapp.web.rest.PasoControlResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /paso-controls?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class PasoControlCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private InstantFilter fechaHora;

    private LongFilter equipoId;

    private LongFilter puntoControlId;

    private LongFilter validadoPorId;

    private Boolean distinct;

    public PasoControlCriteria() {}

    public PasoControlCriteria(PasoControlCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.fechaHora = other.fechaHora == null ? null : other.fechaHora.copy();
        this.equipoId = other.equipoId == null ? null : other.equipoId.copy();
        this.puntoControlId = other.puntoControlId == null ? null : other.puntoControlId.copy();
        this.validadoPorId = other.validadoPorId == null ? null : other.validadoPorId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public PasoControlCriteria copy() {
        return new PasoControlCriteria(this);
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

    public InstantFilter getFechaHora() {
        return fechaHora;
    }

    public InstantFilter fechaHora() {
        if (fechaHora == null) {
            fechaHora = new InstantFilter();
        }
        return fechaHora;
    }

    public void setFechaHora(InstantFilter fechaHora) {
        this.fechaHora = fechaHora;
    }

    public LongFilter getEquipoId() {
        return equipoId;
    }

    public LongFilter equipoId() {
        if (equipoId == null) {
            equipoId = new LongFilter();
        }
        return equipoId;
    }

    public void setEquipoId(LongFilter equipoId) {
        this.equipoId = equipoId;
    }

    public LongFilter getPuntoControlId() {
        return puntoControlId;
    }

    public LongFilter puntoControlId() {
        if (puntoControlId == null) {
            puntoControlId = new LongFilter();
        }
        return puntoControlId;
    }

    public void setPuntoControlId(LongFilter puntoControlId) {
        this.puntoControlId = puntoControlId;
    }

    public LongFilter getValidadoPorId() {
        return validadoPorId;
    }

    public LongFilter validadoPorId() {
        if (validadoPorId == null) {
            validadoPorId = new LongFilter();
        }
        return validadoPorId;
    }

    public void setValidadoPorId(LongFilter validadoPorId) {
        this.validadoPorId = validadoPorId;
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
        final PasoControlCriteria that = (PasoControlCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(fechaHora, that.fechaHora) &&
            Objects.equals(equipoId, that.equipoId) &&
            Objects.equals(puntoControlId, that.puntoControlId) &&
            Objects.equals(validadoPorId, that.validadoPorId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, fechaHora, equipoId, puntoControlId, validadoPorId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PasoControlCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (fechaHora != null ? "fechaHora=" + fechaHora + ", " : "") +
            (equipoId != null ? "equipoId=" + equipoId + ", " : "") +
            (puntoControlId != null ? "puntoControlId=" + puntoControlId + ", " : "") +
            (validadoPorId != null ? "validadoPorId=" + validadoPorId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
