package com.mycompany.myapp.repository.rowmapper;

import com.mycompany.myapp.domain.SevUnidadNegocio;
import io.r2dbc.spi.Row;
import java.util.function.BiFunction;
import org.springframework.stereotype.Service;

/**
 * Converter between {@link Row} to {@link SevUnidadNegocio}, with proper type conversions.
 */
@Service
public class SevUnidadNegocioRowMapper implements BiFunction<Row, String, SevUnidadNegocio> {

    private final ColumnConverter converter;

    public SevUnidadNegocioRowMapper(ColumnConverter converter) {
        this.converter = converter;
    }

    /**
     * Take a {@link Row} and a column prefix, and extract all the fields.
     * @return the {@link SevUnidadNegocio} stored in the database.
     */
    @Override
    public SevUnidadNegocio apply(Row row, String prefix) {
        SevUnidadNegocio entity = new SevUnidadNegocio();
        entity.setId(converter.fromRow(row, prefix + "_id", Long.class));
        entity.setIdUnidadNegocio(converter.fromRow(row, prefix + "_id_unidad_negocio", Integer.class));
        entity.setNombre(converter.fromRow(row, prefix + "_nombre", String.class));
        entity.setFkIdResponsable(converter.fromRow(row, prefix + "_fk_id_responsable", Integer.class));
        return entity;
    }
}
