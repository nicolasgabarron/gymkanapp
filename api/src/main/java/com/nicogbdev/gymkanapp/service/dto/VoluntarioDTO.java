package com.nicogbdev.gymkanapp.service.dto;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

/**
 * A DTO for the {@link com.nicogbdev.gymkanapp.domain.Voluntario} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class VoluntarioDTO implements Serializable {

    private Long id;

    private String dni;

    private String nombre;

    private String apellidos;

    private LocalDate fechaNacimiento;

    private UserDTO usuarioApp;

    private PuntoControlDTO puntoControl;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDni() {
        return dni;
    }

    public void setDni(String dni) {
        this.dni = dni;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellidos() {
        return apellidos;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    public LocalDate getFechaNacimiento() {
        return fechaNacimiento;
    }

    public void setFechaNacimiento(LocalDate fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }

    public UserDTO getUsuarioApp() {
        return usuarioApp;
    }

    public void setUsuarioApp(UserDTO usuarioApp) {
        this.usuarioApp = usuarioApp;
    }

    public PuntoControlDTO getPuntoControl() {
        return puntoControl;
    }

    public void setPuntoControl(PuntoControlDTO puntoControl) {
        this.puntoControl = puntoControl;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof VoluntarioDTO)) {
            return false;
        }

        VoluntarioDTO voluntarioDTO = (VoluntarioDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, voluntarioDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "VoluntarioDTO{" +
            "id=" + getId() +
            ", dni='" + getDni() + "'" +
            ", nombre='" + getNombre() + "'" +
            ", apellidos='" + getApellidos() + "'" +
            ", fechaNacimiento='" + getFechaNacimiento() + "'" +
            ", usuarioApp=" + getUsuarioApp() +
            ", puntoControl=" + getPuntoControl() +
            "}";
    }
}
