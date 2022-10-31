package com.nicogbdev.gymkanapp.service.dto;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

/**
 * A DTO for the {@link com.nicogbdev.gymkanapp.domain.Participante} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ParticipanteDTO implements Serializable {

    private Long id;

    private String dni;

    private String nombre;

    private String apellidos;

    private LocalDate fechaNacimiento;

    private UserDTO usuarioApp;

    private EquipoDTO equipo;

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

    public EquipoDTO getEquipo() {
        return equipo;
    }

    public void setEquipo(EquipoDTO equipo) {
        this.equipo = equipo;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ParticipanteDTO)) {
            return false;
        }

        ParticipanteDTO participanteDTO = (ParticipanteDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, participanteDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ParticipanteDTO{" +
            "id=" + getId() +
            ", dni='" + getDni() + "'" +
            ", nombre='" + getNombre() + "'" +
            ", apellidos='" + getApellidos() + "'" +
            ", fechaNacimiento='" + getFechaNacimiento() + "'" +
            ", usuarioApp=" + getUsuarioApp() +
            ", equipo=" + getEquipo() +
            "}";
    }
}
