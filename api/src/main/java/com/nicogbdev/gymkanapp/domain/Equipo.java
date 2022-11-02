package com.nicogbdev.gymkanapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;

/**
 * A Equipo.
 */
@NamedEntityGraph(
    name = "equipo-entity-graph"
)
@Entity
@Table(name = "equipo")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Equipo implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "identificador")
    private String identificador;

    @Column(name = "nombre")
    private String nombre;

    @OneToMany(mappedBy = "equipo")
    @JsonIgnoreProperties(value = { "usuarioApp", "equipo" }, allowSetters = true)
    private Set<Participante> participantes = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Equipo id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getIdentificador() {
        return this.identificador;
    }

    public Equipo identificador(String identificador) {
        this.setIdentificador(identificador);
        return this;
    }

    public void setIdentificador(String identificador) {
        this.identificador = identificador;
    }

    public String getNombre() {
        return this.nombre;
    }

    public Equipo nombre(String nombre) {
        this.setNombre(nombre);
        return this;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Set<Participante> getParticipantes() {
        return this.participantes;
    }

    public void setParticipantes(Set<Participante> participantes) {
        if (this.participantes != null) {
            this.participantes.forEach(i -> i.setEquipo(null));
        }
        if (participantes != null) {
            participantes.forEach(i -> i.setEquipo(this));
        }
        this.participantes = participantes;
    }

    public Equipo participantes(Set<Participante> participantes) {
        this.setParticipantes(participantes);
        return this;
    }

    public Equipo addParticipantes(Participante participante) {
        this.participantes.add(participante);
        participante.setEquipo(this);
        return this;
    }

    public Equipo removeParticipantes(Participante participante) {
        this.participantes.remove(participante);
        participante.setEquipo(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Equipo)) {
            return false;
        }
        return id != null && id.equals(((Equipo) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Equipo{" +
            "id=" + getId() +
            ", identificador='" + getIdentificador() + "'" +
            ", nombre='" + getNombre() + "'" +
            "}";
    }
}
