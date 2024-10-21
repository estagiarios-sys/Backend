package com.systextil.relatorio.domain.table;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

class TableRepository {

	Map<String, Map<String, String>> getTablesFromDataBase(Connection idConnection, String catalog, String tableNamePattern) throws SQLException {
        Map<String, Map<String, String>> tablesAndColumns = new HashMap<>();
        DatabaseMetaData metaData = idConnection.getMetaData();
        ResultSet data = metaData.getTables(catalog, metaData.getUserName(), tableNamePattern, new String[]{"TABLE"});

        while (data.next()) {
            String tableName = data.getString("TABLE_NAME");

            ResultSet columns = metaData.getColumns(null, null, tableName, "%");
            Map<String, String> columnNames = new LinkedHashMap<>();
            while (columns.next()) {
                String columnName = columns.getString("COLUMN_NAME");
                String columnType = columns.getString("TYPE_NAME");
                if (columnName != null) {
                    columnNames.put(columnName, columnType);
                }
            }
            tablesAndColumns.put(tableName, columnNames);
        }

        return tablesAndColumns;
    }
}