package com.mycompany.myapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.LocalDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

/**
 * A SevGraficaData.
 */
@Table("sev_grafica_data")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class SevGraficaData implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column("id")
    private Long id;

    @NotNull(message = "must not be null")
    @Column("id_row")
    private Integer idRow;

    @NotNull(message = "must not be null")
    @Column("fecha_objetivo")
    private LocalDate fechaObjetivo;

    @NotNull(message = "must not be null")
    @Column("valor_objetivo")
    private Double valorObjetivo;

    @NotNull(message = "must not be null")
    @Column("valor_logrado")
    private Double valorLogrado;

    @Transient
    @JsonIgnoreProperties(value = { "proceso" }, allowSetters = true)
    private SevGrafica grafica;

    @Column("grafica_id")
    private Long graficaId;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public SevGraficaData id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getIdRow() {
        return this.idRow;
    }

    public SevGraficaData idRow(Integer idRow) {
        this.setIdRow(idRow);
        return this;
    }

    public void setIdRow(Integer idRow) {
        this.idRow = idRow;
    }

    public LocalDate getFechaObjetivo() {
        return this.fechaObjetivo;
    }

    public SevGraficaData fechaObjetivo(LocalDate fechaObjetivo) {
        this.setFechaObjetivo(fechaObjetivo);
        return this;
    }

    public void setFechaObjetivo(LocalDate fechaObjetivo) {
        this.fechaObjetivo = fechaObjetivo;
    }

    public Double getValorObjetivo() {
        return this.valorObjetivo;
    }

    public SevGraficaData valorObjetivo(Double valorObjetivo) {
        this.setValorObjetivo(valorObjetivo);
        return this;
    }

    public void setValorObjetivo(Double valorObjetivo) {
        this.valorObjetivo = valorObjetivo;
    }

    public Double getValorLogrado() {
        return this.valorLogrado;
    }

    public SevGraficaData valorLogrado(Double valorLogrado) {
        this.setValorLogrado(valorLogrado);
        return this;
    }

    public void setValorLogrado(Double valorLogrado) {
        this.valorLogrado = valorLogrado;
    }

    public SevGrafica getGrafica() {
        return this.grafica;
    }

    public void setGrafica(SevGrafica sevGrafica) {
        this.grafica = sevGrafica;
        this.graficaId = sevGrafica != null ? sevGrafica.getId() : null;
    }

    public SevGraficaData grafica(SevGrafica sevGrafica) {
        this.setGrafica(sevGrafica);
        return this;
    }

    public Long getGraficaId() {
        return this.graficaId;
    }

    public void setGraficaId(Long sevGrafica) {
        this.graficaId = sevGrafica;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof SevGraficaData)) {
            return false;
        }
        return getId() != null && getId().equals(((SevGraficaData) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "SevGraficaData{" +
            "id=" + getId() +
            ", idRow=" + getIdRow() +
            ", fechaObjetivo='" + getFechaObjetivo() + "'" +
            ", valorObjetivo=" + getValorObjetivo() +
            ", valorLogrado=" + getValorLogrado() +
            "}";
    }
}
