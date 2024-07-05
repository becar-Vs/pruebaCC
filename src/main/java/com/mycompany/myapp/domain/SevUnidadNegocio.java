package com.mycompany.myapp.domain;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

/**
 * A SevUnidadNegocio.
 */
@Table("sev_unidad_negocio")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class SevUnidadNegocio implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column("id")
    private Long id;

    @NotNull(message = "must not be null")
    @Column("id_unidad_negocio")
    private Integer idUnidadNegocio;

    @NotNull(message = "must not be null")
    @Column("nombre")
    private String nombre;

    @NotNull(message = "must not be null")
    @Column("fk_id_responsable")
    private Integer fkIdResponsable;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public SevUnidadNegocio id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getIdUnidadNegocio() {
        return this.idUnidadNegocio;
    }

    public SevUnidadNegocio idUnidadNegocio(Integer idUnidadNegocio) {
        this.setIdUnidadNegocio(idUnidadNegocio);
        return this;
    }

    public void setIdUnidadNegocio(Integer idUnidadNegocio) {
        this.idUnidadNegocio = idUnidadNegocio;
    }

    public String getNombre() {
        return this.nombre;
    }

    public SevUnidadNegocio nombre(String nombre) {
        this.setNombre(nombre);
        return this;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Integer getFkIdResponsable() {
        return this.fkIdResponsable;
    }

    public SevUnidadNegocio fkIdResponsable(Integer fkIdResponsable) {
        this.setFkIdResponsable(fkIdResponsable);
        return this;
    }

    public void setFkIdResponsable(Integer fkIdResponsable) {
        this.fkIdResponsable = fkIdResponsable;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof SevUnidadNegocio)) {
            return false;
        }
        return getId() != null && getId().equals(((SevUnidadNegocio) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "SevUnidadNegocio{" +
            "id=" + getId() +
            ", idUnidadNegocio=" + getIdUnidadNegocio() +
            ", nombre='" + getNombre() + "'" +
            ", fkIdResponsable=" + getFkIdResponsable() +
            "}";
    }
}
