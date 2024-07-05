package com.mycompany.myapp.repository.rowmapper;

import com.mycompany.myapp.domain.SevGrafica;
import io.r2dbc.spi.Row;
import java.util.function.BiFunction;
import org.springframework.stereotype.Service;

/**
 * Converter between {@link Row} to {@link SevGrafica}, with proper type conversions.
 */
@Service
public class SevGraficaRowMapper implements BiFunction<Row, String, SevGrafica> {

    private final ColumnConverter converter;

    public SevGraficaRowMapper(ColumnConverter converter) {
        this.converter = converter;
    }

    /**
     * Take a {@link Row} and a column prefix, and extract all the fields.
     * @return the {@link SevGrafica} stored in the database.
     */
    @Override
    public SevGrafica apply(Row row, String prefix) {
        SevGrafica entity = new SevGrafica();
        entity.setId(converter.fromRow(row, prefix + "_id", Long.class));
        entity.setIdGrafica(converter.fromRow(row, prefix + "_id_grafica", Integer.class));
        entity.setNombre(converter.fromRow(row, prefix + "_nombre", String.class));
        entity.setFkIdResponsable(converter.fromRow(row, prefix + "_fk_id_responsable", Integer.class));
        entity.setProcesoId(converter.fromRow(row, prefix + "_proceso_id", Long.class));
        return entity;
    }
}
