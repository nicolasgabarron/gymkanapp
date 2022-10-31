package com.nicogbdev.gymkanapp.service.dto;

import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.nicogbdev.gymkanapp.domain.PuntoControl} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class PuntoControlDTO implements Serializable {

    private Long id;

    private String identificador;

    private Integer orden;

    private String nombre;

    private String direccion;

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

    public Integer getOrden() {
        return orden;
    }

    public void setOrden(Integer orden) {
        this.orden = orden;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PuntoControlDTO)) {
            return false;
        }

        PuntoControlDTO puntoControlDTO = (PuntoControlDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, puntoControlDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PuntoControlDTO{" +
            "id=" + getId() +
            ", identificador='" + getIdentificador() + "'" +
            ", orden=" + getOrden() +
            ", nombre='" + getNombre() + "'" +
            ", direccion='" + getDireccion() + "'" +
            "}";
    }
}
