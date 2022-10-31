package com.nicogbdev.gymkanapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;

/**
 * A PuntoControl.
 */
@NamedEntityGraph(
    name = "punto-control-entity-graph",
    attributeNodes = {
        @NamedAttributeNode("voluntarios")
    }
)
@Entity
@Table(name = "punto_control")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class PuntoControl implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "identificador")
    private String identificador;

    @Column(name = "orden")
    private Integer orden;

    @Column(name = "nombre")
    private String nombre;

    @Column(name = "direccion")
    private String direccion;

    @OneToMany(mappedBy = "puntoControl")
    @JsonIgnoreProperties(value = { "usuarioApp", "puntoControl" }, allowSetters = true)
    private Set<Voluntario> voluntarios = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public PuntoControl id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getIdentificador() {
        return this.identificador;
    }

    public PuntoControl identificador(String identificador) {
        this.setIdentificador(identificador);
        return this;
    }

    public void setIdentificador(String identificador) {
        this.identificador = identificador;
    }

    public Integer getOrden() {
        return this.orden;
    }

    public PuntoControl orden(Integer orden) {
        this.setOrden(orden);
        return this;
    }

    public void setOrden(Integer orden) {
        this.orden = orden;
    }

    public String getNombre() {
        return this.nombre;
    }

    public PuntoControl nombre(String nombre) {
        this.setNombre(nombre);
        return this;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDireccion() {
        return this.direccion;
    }

    public PuntoControl direccion(String direccion) {
        this.setDireccion(direccion);
        return this;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public Set<Voluntario> getVoluntarios() {
        return this.voluntarios;
    }

    public void setVoluntarios(Set<Voluntario> voluntarios) {
        if (this.voluntarios != null) {
            this.voluntarios.forEach(i -> i.setPuntoControl(null));
        }
        if (voluntarios != null) {
            voluntarios.forEach(i -> i.setPuntoControl(this));
        }
        this.voluntarios = voluntarios;
    }

    public PuntoControl voluntarios(Set<Voluntario> voluntarios) {
        this.setVoluntarios(voluntarios);
        return this;
    }

    public PuntoControl addVoluntarios(Voluntario voluntario) {
        this.voluntarios.add(voluntario);
        voluntario.setPuntoControl(this);
        return this;
    }

    public PuntoControl removeVoluntarios(Voluntario voluntario) {
        this.voluntarios.remove(voluntario);
        voluntario.setPuntoControl(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PuntoControl)) {
            return false;
        }
        return id != null && id.equals(((PuntoControl) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PuntoControl{" +
            "id=" + getId() +
            ", identificador='" + getIdentificador() + "'" +
            ", orden=" + getOrden() +
            ", nombre='" + getNombre() + "'" +
            ", direccion='" + getDireccion() + "'" +
            "}";
    }
}
