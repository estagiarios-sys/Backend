package com.systextil.relatorio.domain.table;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

@Repository
class TableRepository {

	List<String> getTables(Connection idConnection, String catalog) throws SQLException {
        List<String> tables = new ArrayList<>();
        DatabaseMetaData metaData = idConnection.getMetaData();
        ResultSet data = metaData.getTables(catalog, metaData.getUserName(), "%", new String[]{"TABLE"});

        while (data.next()) {
            String tableName = data.getString("TABLE_NAME");

            tables.add(tableName);
        }
        return tables;
    }
	
	Map<String, String> getColumnsFromTable(Connection idConnection, String table) throws SQLException {
		DatabaseMetaData metaData = idConnection.getMetaData();
		ResultSet tableData = metaData.getColumns(null, null, table, "%");
		Map<String, String> columns = new LinkedHashMap<>();
		
		while(tableData.next()) {
			columns.put(tableData.getString("COLUMN_NAME"), tableData.getString("TYPE_NAME"));
		}
		return columns;
	}
}