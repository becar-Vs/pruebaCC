package com.mycompany.myapp.domain;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

/**
 * A SevProceso.
 */
@Table("sev_proceso")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class SevProceso implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column("id")
    private Long id;

    @NotNull(message = "must not be null")
    @Column("id_proceso")
    private Integer idProceso;

    @NotNull(message = "must not be null")
    @Column("nombre")
    private String nombre;

    @NotNull(message = "must not be null")
    @Column("fk_id_responsable")
    private Integer fkIdResponsable;

    @Transient
    private SevUnidadNegocio unidadNegocio;

    @Column("unidad_negocio_id")
    private Long unidadNegocioId;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public SevProceso id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getIdProceso() {
        return this.idProceso;
    }

    public SevProceso idProceso(Integer idProceso) {
        this.setIdProceso(idProceso);
        return this;
    }

    public void setIdProceso(Integer idProceso) {
        this.idProceso = idProceso;
    }

    public String getNombre() {
        return this.nombre;
    }

    public SevProceso nombre(String nombre) {
        this.setNombre(nombre);
        return this;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Integer getFkIdResponsable() {
        return this.fkIdResponsable;
    }

    public SevProceso fkIdResponsable(Integer fkIdResponsable) {
        this.setFkIdResponsable(fkIdResponsable);
        return this;
    }

    public void setFkIdResponsable(Integer fkIdResponsable) {
        this.fkIdResponsable = fkIdResponsable;
    }

    public SevUnidadNegocio getUnidadNegocio() {
        return this.unidadNegocio;
    }

    public void setUnidadNegocio(SevUnidadNegocio sevUnidadNegocio) {
        this.unidadNegocio = sevUnidadNegocio;
        this.unidadNegocioId = sevUnidadNegocio != null ? sevUnidadNegocio.getId() : null;
    }

    public SevProceso unidadNegocio(SevUnidadNegocio sevUnidadNegocio) {
        this.setUnidadNegocio(sevUnidadNegocio);
        return this;
    }

    public Long getUnidadNegocioId() {
        return this.unidadNegocioId;
    }

    public void setUnidadNegocioId(Long sevUnidadNegocio) {
        this.unidadNegocioId = sevUnidadNegocio;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof SevProceso)) {
            return false;
        }
        return getId() != null && getId().equals(((SevProceso) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "SevProceso{" +
            "id=" + getId() +
            ", idProceso=" + getIdProceso() +
            ", nombre='" + getNombre() + "'" +
            ", fkIdResponsable=" + getFkIdResponsable() +
            "}";
    }
}
