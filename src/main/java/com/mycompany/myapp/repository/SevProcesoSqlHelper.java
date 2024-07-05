package com.mycompany.myapp.repository;

import java.util.ArrayList;
import java.util.List;
import org.springframework.data.relational.core.sql.Column;
import org.springframework.data.relational.core.sql.Expression;
import org.springframework.data.relational.core.sql.Table;

public class SevProcesoSqlHelper {

    public static List<Expression> getColumns(Table table, String columnPrefix) {
        List<Expression> columns = new ArrayList<>();
        columns.add(Column.aliased("id", table, columnPrefix + "_id"));
        columns.add(Column.aliased("id_proceso", table, columnPrefix + "_id_proceso"));
        columns.add(Column.aliased("nombre", table, columnPrefix + "_nombre"));
        columns.add(Column.aliased("fk_id_responsable", table, columnPrefix + "_fk_id_responsable"));

        columns.add(Column.aliased("unidad_negocio_id", table, columnPrefix + "_unidad_negocio_id"));
        return columns;
    }
}
