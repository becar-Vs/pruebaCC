package com.mycompany.myapp.repository.rowmapper;

import com.mycompany.myapp.domain.SevProceso;
import io.r2dbc.spi.Row;
import java.util.function.BiFunction;
import org.springframework.stereotype.Service;

/**
 * Converter between {@link Row} to {@link SevProceso}, with proper type conversions.
 */
@Service
public class SevProcesoRowMapper implements BiFunction<Row, String, SevProceso> {

    private final ColumnConverter converter;

    public SevProcesoRowMapper(ColumnConverter converter) {
        this.converter = converter;
    }

    /**
     * Take a {@link Row} and a column prefix, and extract all the fields.
     * @return the {@link SevProceso} stored in the database.
     */
    @Override
    public SevProceso apply(Row row, String prefix) {
        SevProceso entity = new SevProceso();
        entity.setId(converter.fromRow(row, prefix + "_id", Long.class));
        entity.setIdProceso(converter.fromRow(row, prefix + "_id_proceso", Integer.class));
        entity.setNombre(converter.fromRow(row, prefix + "_nombre", String.class));
        entity.setFkIdResponsable(converter.fromRow(row, prefix + "_fk_id_responsable", Integer.class));
        entity.setUnidadNegocioId(converter.fromRow(row, prefix + "_unidad_negocio_id", Long.class));
        return entity;
    }
}
