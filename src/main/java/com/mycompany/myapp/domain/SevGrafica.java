package com.mycompany.myapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

/**
 * A SevGrafica.
 */
@Table("sev_grafica")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class SevGrafica implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column("id")
    private Long id;

    @NotNull(message = "must not be null")
    @Column("id_grafica")
    private Integer idGrafica;

    @NotNull(message = "must not be null")
    @Column("nombre")
    private String nombre;

    @NotNull(message = "must not be null")
    @Column("fk_id_responsable")
    private Integer fkIdResponsable;

    @Transient
    @JsonIgnoreProperties(value = { "unidadNegocio" }, allowSetters = true)
    private SevProceso proceso;

    @Column("proceso_id")
    private Long procesoId;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public SevGrafica id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getIdGrafica() {
        return this.idGrafica;
    }

    public SevGrafica idGrafica(Integer idGrafica) {
        this.setIdGrafica(idGrafica);
        return this;
    }

    public void setIdGrafica(Integer idGrafica) {
        this.idGrafica = idGrafica;
    }

    public String getNombre() {
        return this.nombre;
    }

    public SevGrafica nombre(String nombre) {
        this.setNombre(nombre);
        return this;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Integer getFkIdResponsable() {
        return this.fkIdResponsable;
    }

    public SevGrafica fkIdResponsable(Integer fkIdResponsable) {
        this.setFkIdResponsable(fkIdResponsable);
        return this;
    }

    public void setFkIdResponsable(Integer fkIdResponsable) {
        this.fkIdResponsable = fkIdResponsable;
    }

    public SevProceso getProceso() {
        return this.proceso;
    }

    public void setProceso(SevProceso sevProceso) {
        this.proceso = sevProceso;
        this.procesoId = sevProceso != null ? sevProceso.getId() : null;
    }

    public SevGrafica proceso(SevProceso sevProceso) {
        this.setProceso(sevProceso);
        return this;
    }

    public Long getProcesoId() {
        return this.procesoId;
    }

    public void setProcesoId(Long sevProceso) {
        this.procesoId = sevProceso;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof SevGrafica)) {
            return false;
        }
        return getId() != null && getId().equals(((SevGrafica) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "SevGrafica{" +
            "id=" + getId() +
            ", idGrafica=" + getIdGrafica() +
            ", nombre='" + getNombre() + "'" +
            ", fkIdResponsable=" + getFkIdResponsable() +
            "}";
    }
}
