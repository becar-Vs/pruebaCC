package com.mycompany.myapp.repository;

import java.util.ArrayList;
import java.util.List;
import org.springframework.data.relational.core.sql.Column;
import org.springframework.data.relational.core.sql.Expression;
import org.springframework.data.relational.core.sql.Table;

public class SevGraficaDataSqlHelper {

    public static List<Expression> getColumns(Table table, String columnPrefix) {
        List<Expression> columns = new ArrayList<>();
        columns.add(Column.aliased("id", table, columnPrefix + "_id"));
        columns.add(Column.aliased("id_row", table, columnPrefix + "_id_row"));
        columns.add(Column.aliased("fecha_objetivo", table, columnPrefix + "_fecha_objetivo"));
        columns.add(Column.aliased("valor_objetivo", table, columnPrefix + "_valor_objetivo"));
        columns.add(Column.aliased("valor_logrado", table, columnPrefix + "_valor_logrado"));

        columns.add(Column.aliased("grafica_id", table, columnPrefix + "_grafica_id"));
        return columns;
    }
}
