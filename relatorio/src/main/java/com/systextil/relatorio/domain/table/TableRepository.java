package com.systextil.relatorio.domain.table;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

class TableRepository {

	List<String> getTables(Connection idConnection, String catalog, String tableNamePattern) throws SQLException {
        List<String> tables = new ArrayList<>();
        DatabaseMetaData metaData = idConnection.getMetaData();
        ResultSet data = metaData.getTables(catalog, metaData.getUserName(), tableNamePattern, new String[]{"TABLE"});

        while (data.next()) {
            String tableName = data.getString("TABLE_NAME");

            tables.add(tableName);
        }
        return tables;
    }
	
	Map<String, Map<String, String>> getColumnsFromTables(Connection idConnection, AllTables allTables) throws SQLException {
		Map<String, Map<String, String>> tablesAndColumns = new LinkedHashMap<>();
		DatabaseMetaData metaData = idConnection.getMetaData();
		ResultSet mainTableData = metaData.getColumns(null, null, allTables.mainTable(), "%");
		Map<String, String> mainTableColumns = new LinkedHashMap<>();
		
		while(mainTableData.next()) {
			mainTableColumns.put(mainTableData.getString("COLUMN_NAME"), mainTableData.getString("TYPE_NAME"));
		}
		tablesAndColumns.put(allTables.mainTable(), mainTableColumns);
		
		for (String tablesPair : allTables.tablesPairs()) {
			Pattern pattern = Pattern.compile("\\b\\w+$");
			Matcher matcher = pattern.matcher(tablesPair);
			matcher.find();
			String table = matcher.group();
			
			ResultSet tableData = metaData.getColumns(null, null, table, "%");
			Map<String, String> tableColumns = new LinkedHashMap<>();
			
			while(tableData.next()) {
				tableColumns.put(tableData.getString("COLUMN_NAME"), tableData.getString("TYPE_NAME"));
			}
			tablesAndColumns.put(table, tableColumns);
		}
		return tablesAndColumns;
	}
}