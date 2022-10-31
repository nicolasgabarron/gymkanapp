package com.nicogbdev.gymkanapp.service.dto;

import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * A DTO for the {@link com.nicogbdev.gymkanapp.domain.PasoControl} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class PasoControlDTO implements Serializable {

    private Long id;

    private Instant fechaHora;

    private EquipoDTO equipo;

    private PuntoControlDTO puntoControl;

    private VoluntarioDTO validadoPor;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Instant getFechaHora() {
        return fechaHora;
    }

    public void setFechaHora(Instant fechaHora) {
        this.fechaHora = fechaHora;
    }

    public EquipoDTO getEquipo() {
        return equipo;
    }

    public void setEquipo(EquipoDTO equipo) {
        this.equipo = equipo;
    }

    public PuntoControlDTO getPuntoControl() {
        return puntoControl;
    }

    public void setPuntoControl(PuntoControlDTO puntoControl) {
        this.puntoControl = puntoControl;
    }

    public VoluntarioDTO getValidadoPor() {
        return validadoPor;
    }

    public void setValidadoPor(VoluntarioDTO validadoPor) {
        this.validadoPor = validadoPor;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PasoControlDTO)) {
            return false;
        }

        PasoControlDTO pasoControlDTO = (PasoControlDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, pasoControlDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PasoControlDTO{" +
            "id=" + getId() +
            ", fechaHora='" + getFechaHora() + "'" +
            ", equipo=" + getEquipo() +
            ", puntoControl=" + getPuntoControl() +
            ", validadoPor=" + getValidadoPor() +
            "}";
    }
}
