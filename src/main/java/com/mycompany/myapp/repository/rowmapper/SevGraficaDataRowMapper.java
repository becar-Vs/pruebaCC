package com.mycompany.myapp.repository.rowmapper;

import com.mycompany.myapp.domain.SevGraficaData;
import io.r2dbc.spi.Row;
import java.time.LocalDate;
import java.util.function.BiFunction;
import org.springframework.stereotype.Service;

/**
 * Converter between {@link Row} to {@link SevGraficaData}, with proper type conversions.
 */
@Service
public class SevGraficaDataRowMapper implements BiFunction<Row, String, SevGraficaData> {

    private final ColumnConverter converter;

    public SevGraficaDataRowMapper(ColumnConverter converter) {
        this.converter = converter;
    }

    /**
     * Take a {@link Row} and a column prefix, and extract all the fields.
     * @return the {@link SevGraficaData} stored in the database.
     */
    @Override
    public SevGraficaData apply(Row row, String prefix) {
        SevGraficaData entity = new SevGraficaData();
        entity.setId(converter.fromRow(row, prefix + "_id", Long.class));
        entity.setIdRow(converter.fromRow(row, prefix + "_id_row", Integer.class));
        entity.setFechaObjetivo(converter.fromRow(row, prefix + "_fecha_objetivo", LocalDate.class));
        entity.setValorObjetivo(converter.fromRow(row, prefix + "_valor_objetivo", Double.class));
        entity.setValorLogrado(converter.fromRow(row, prefix + "_valor_logrado", Double.class));
        entity.setGraficaId(converter.fromRow(row, prefix + "_grafica_id", Long.class));
        return entity;
    }
}
