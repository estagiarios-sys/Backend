package com.systextil.relatorio.domain.data_base_data;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

class DataBaseDataRepository {

    private PreparedStatement command;
    private ResultSet data;
    
    LoadedQueryData findDataByQuery(Connection idConnection, String finalQuery, String totalizersQuery) throws SQLException {
    	LoadedQueryData loadedQueryData = findDataByFinalQuery(idConnection, finalQuery);
    	
    	if (totalizersQuery != null && !totalizersQuery.isBlank()) {
    		ArrayList<String> totalizersResults = findDataByTotalizersQuery(idConnection, totalizersQuery);
        	return new LoadedQueryData(loadedQueryData.columnsNameAndNickName(), loadedQueryData.foundObjects(), totalizersResults);
    	}
    	
    	return loadedQueryData;
    }

    private LoadedQueryData findDataByFinalQuery(Connection idConnection, String sql) throws SQLException {
        ArrayList<Object[]> listObjects = new ArrayList<>();
        Map<String, String> columnsNameAndNickName = new LinkedHashMap<>();
        ArrayList<String> tableNames = new ArrayList<>();
        ArrayList<String> columnNames = new ArrayList<>();
        Pattern tableDotColumnPattern = Pattern.compile("(\\w+)\\.(\\w+)", Pattern.CASE_INSENSITIVE);
        Matcher tableDotColumnMatcher = tableDotColumnPattern.matcher(sql);
        
        while (tableDotColumnMatcher.find()) {
            String tableName = tableDotColumnMatcher.group(1);
            String columnName = tableDotColumnMatcher.group(2);
            tableNames.add(tableName);
            columnNames.add(columnName);
        }
        command = idConnection.prepareStatement(sql);
        data = command.executeQuery();
        ResultSetMetaData metaData = data.getMetaData();
        int columnsNumber = metaData.getColumnCount();

        for (int i = 1; i <= columnsNumber; i++) {
            String columnNickName = metaData.getColumnLabel(i);
            String columnName = columnNames.get(i-1);

            if (columnNickName.equalsIgnoreCase(columnName)) {
                columnsNameAndNickName.put(tableNames.get(i-1) + "." + columnName, null);
            } else {
                columnsNameAndNickName.put(tableNames.get(i-1) + "." + columnName, columnNickName);
            }
        }

        while (data.next()) {
            Object[] object = new Object[columnsNumber];

            for (int i = 1; i <= columnsNumber; i++) {
                object[i - 1] = data.getString(i);
            }
            listObjects.add(object);
        }
        
        return new LoadedQueryData(columnsNameAndNickName, listObjects, null);
    }
    
    private ArrayList<String> findDataByTotalizersQuery(Connection idConnection, String totalizersQuery) throws SQLException {
    	ArrayList<String> totalizersResults = new ArrayList<>();
    	command = idConnection.prepareStatement(totalizersQuery);
    	data = command.executeQuery();
    	ResultSetMetaData metaData = data.getMetaData();
    	int columnsNumber = metaData.getColumnCount();
    	data.next();
    	
    	for (int i = 1; i <= columnsNumber; i++) {
    		totalizersResults.add(String.valueOf(data.getInt(i)));
    	}
    	    	
    	return totalizersResults;
    }
    
    Map<String, Map<String, String>> getTablesAndColumnsFromDataBase(Connection idConnection, String catalog, String tableNamePattern) throws SQLException {
        Map<String, Map<String, String>> tablesAndColumns = new HashMap<>();
        DatabaseMetaData metaData = idConnection.getMetaData();
        data = metaData.getTables(catalog, metaData.getUserName(), tableNamePattern, new String[]{"TABLE"});

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
    
    ArrayList<RelationshipData> getRelationshipsFromDataBase(Connection idConnection, String sql) throws SQLException {
    	command = idConnection.prepareStatement(sql);
        data = command.executeQuery();
        ArrayList<RelationshipData> listRelationshipData = new ArrayList<>();

        while (data.next()) {
            String tableName = data.getString("TABLE_NAME");
            String columnName = data.getString("COLUMN_NAME");
            String referencedTableName = data.getString("REFERENCED_TABLE_NAME");
            String referencedColumnName = data.getString("REFERENCED_COLUMN_NAME");
            String tableAndReferencedTable = tableName + " e " + referencedTableName;
            String join = "INNER JOIN " + referencedTableName + " ON " + tableName + "." + columnName + " = " + referencedTableName + "." + referencedColumnName;
            RelationshipData relationshipData = new RelationshipData(tableAndReferencedTable, join);
            listRelationshipData.add(relationshipData);
            String tableAndReferencedTableReversed = referencedTableName + " e " + tableName;
            String joinReversed = "INNER JOIN " + tableName + " ON " + referencedTableName + "." + referencedColumnName + " = " + tableName + "." + columnName;
            RelationshipData relationshipDataReversed = new RelationshipData(tableAndReferencedTableReversed, joinReversed);
            listRelationshipData.add(relationshipDataReversed);
        }
        
        return listRelationshipData;
    }
}