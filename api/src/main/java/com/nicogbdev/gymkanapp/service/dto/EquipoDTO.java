package com.nicogbdev.gymkanapp.service.dto;

import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.nicogbdev.gymkanapp.domain.Equipo} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class EquipoDTO implements Serializable {

    private Long id;

    private String identificador;

    private String nombre;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getIdentificador() {
        return identificador;
    }

    public void setIdentificador(String identificador) {
        this.identificador = identificador;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof EquipoDTO)) {
            return false;
        }

        EquipoDTO equipoDTO = (EquipoDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, equipoDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "EquipoDTO{" +
            "id=" + getId() +
            ", identificador='" + getIdentificador() + "'" +
            ", nombre='" + getNombre() + "'" +
            "}";
    }
}
