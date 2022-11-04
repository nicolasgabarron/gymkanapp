package com.nicogbdev.gymkanapp.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import org.springdoc.api.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.nicogbdev.gymkanapp.domain.Participante} entity. This class is used
 * in {@link com.nicogbdev.gymkanapp.web.rest.ParticipanteResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /participantes?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ParticipanteCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter dni;

    private StringFilter nombre;

    private StringFilter apellidos;

    private LocalDateFilter fechaNacimiento;

    private LongFilter usuarioAppId;

    private LongFilter equipoId;

    private Boolean distinct;

    public ParticipanteCriteria() {}

    public ParticipanteCriteria(ParticipanteCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.dni = other.dni == null ? null : other.dni.copy();
        this.nombre = other.nombre == null ? null : other.nombre.copy();
        this.apellidos = other.apellidos == null ? null : other.apellidos.copy();
        this.fechaNacimiento = other.fechaNacimiento == null ? null : other.fechaNacimiento.copy();
        this.usuarioAppId = other.usuarioAppId == null ? null : other.usuarioAppId.copy();
        this.equipoId = other.equipoId == null ? null : other.equipoId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public ParticipanteCriteria copy() {
        return new ParticipanteCriteria(this);
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

    public StringFilter getDni() {
        return dni;
    }

    public StringFilter dni() {
        if (dni == null) {
            dni = new StringFilter();
        }
        return dni;
    }

    public void setDni(StringFilter dni) {
        this.dni = dni;
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

    public StringFilter getApellidos() {
        return apellidos;
    }

    public StringFilter apellidos() {
        if (apellidos == null) {
            apellidos = new StringFilter();
        }
        return apellidos;
    }

    public void setApellidos(StringFilter apellidos) {
        this.apellidos = apellidos;
    }

    public LocalDateFilter getFechaNacimiento() {
        return fechaNacimiento;
    }

    public LocalDateFilter fechaNacimiento() {
        if (fechaNacimiento == null) {
            fechaNacimiento = new LocalDateFilter();
        }
        return fechaNacimiento;
    }

    public void setFechaNacimiento(LocalDateFilter fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }

    public LongFilter getUsuarioAppId() {
        return usuarioAppId;
    }

    public LongFilter usuarioAppId() {
        if (usuarioAppId == null) {
            usuarioAppId = new LongFilter();
        }
        return usuarioAppId;
    }

    public void setUsuarioAppId(LongFilter usuarioAppId) {
        this.usuarioAppId = usuarioAppId;
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
        final ParticipanteCriteria that = (ParticipanteCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(dni, that.dni) &&
            Objects.equals(nombre, that.nombre) &&
            Objects.equals(apellidos, that.apellidos) &&
            Objects.equals(fechaNacimiento, that.fechaNacimiento) &&
            Objects.equals(usuarioAppId, that.usuarioAppId) &&
            Objects.equals(equipoId, that.equipoId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, dni, nombre, apellidos, fechaNacimiento, usuarioAppId, equipoId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ParticipanteCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (dni != null ? "dni=" + dni + ", " : "") +
            (nombre != null ? "nombre=" + nombre + ", " : "") +
            (apellidos != null ? "apellidos=" + apellidos + ", " : "") +
            (fechaNacimiento != null ? "fechaNacimiento=" + fechaNacimiento + ", " : "") +
            (usuarioAppId != null ? "usuarioAppId=" + usuarioAppId + ", " : "") +
            (equipoId != null ? "equipoId=" + equipoId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
