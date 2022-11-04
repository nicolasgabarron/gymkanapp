package com.nicogbdev.gymkanapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.Instant;
import javax.persistence.*;

/**
 * A PasoControl.
 */
@NamedEntityGraph(
    name = "paso-control-entity-graph",
    attributeNodes = {
        @NamedAttributeNode("equipo"),
        @NamedAttributeNode("puntoControl"),
        @NamedAttributeNode("validadoPor"),
    }
)
@Entity
@Table(name = "paso_control")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class PasoControl implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "fecha_hora")
    private Instant fechaHora;

    @ManyToOne
    @JsonIgnoreProperties(value = { "participantes" }, allowSetters = true)
    private Equipo equipo;

    @ManyToOne
    @JsonIgnoreProperties(value = { "voluntarios" }, allowSetters = true)
    private PuntoControl puntoControl;

    @ManyToOne
    @JsonIgnoreProperties(value = { "usuarioApp", "puntoControl" }, allowSetters = true)
    private Voluntario validadoPor;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public PasoControl id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Instant getFechaHora() {
        return this.fechaHora;
    }

    public PasoControl fechaHora(Instant fechaHora) {
        this.setFechaHora(fechaHora);
        return this;
    }

    public void setFechaHora(Instant fechaHora) {
        this.fechaHora = fechaHora;
    }

    public Equipo getEquipo() {
        return this.equipo;
    }

    public void setEquipo(Equipo equipo) {
        this.equipo = equipo;
    }

    public PasoControl equipo(Equipo equipo) {
        this.setEquipo(equipo);
        return this;
    }

    public PuntoControl getPuntoControl() {
        return this.puntoControl;
    }

    public void setPuntoControl(PuntoControl puntoControl) {
        this.puntoControl = puntoControl;
    }

    public PasoControl puntoControl(PuntoControl puntoControl) {
        this.setPuntoControl(puntoControl);
        return this;
    }

    public Voluntario getValidadoPor() {
        return this.validadoPor;
    }

    public void setValidadoPor(Voluntario voluntario) {
        this.validadoPor = voluntario;
    }

    public PasoControl validadoPor(Voluntario voluntario) {
        this.setValidadoPor(voluntario);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PasoControl)) {
            return false;
        }
        return id != null && id.equals(((PasoControl) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PasoControl{" +
            "id=" + getId() +
            ", fechaHora='" + getFechaHora() + "'" +
            "}";
    }
}
