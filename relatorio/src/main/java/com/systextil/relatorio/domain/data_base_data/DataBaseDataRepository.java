package com.systextil.relatorio.domain.data_base_data;

import com.systextil.relatorio.infra.data_base_connection.ConnectionMySQL;
import com.systextil.relatorio.infra.data_base_connection.ConnectionOracle;
import com.systextil.relatorio.infra.exception_handler.ActualTimeNotFoundException;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

class DataBaseDataRepository {

    private ConnectionMySQL connectionMySQL;
    private ConnectionOracle connectionOracle;
    private PreparedStatement command;
    private ResultSet data;

    LoadedQueryData findDataByQueryFromOracleDataBase(String finalQuery, String totalizersQuery) throws SQLException {
    	connectionOracle = new ConnectionOracle();
    	connectionOracle.connect();
    	LoadedQueryData loadedQueryData = findDataByQuery(connectionOracle.getIdConnection(), finalQuery);
    	
    	if (totalizersQuery != null && !totalizersQuery.isBlank()) {
    		ArrayList<String> totalizersResults = getTotalizersResults(connectionOracle.getIdConnection(), totalizersQuery);
        	LoadedQueryData loadedQueryDataWithTotalizersResults = new LoadedQueryData(loadedQueryData.columnsNameAndNickName(), loadedQueryData.foundObjects(), totalizersResults);
        	connectionOracle.disconnect();
        	        	
        	return loadedQueryDataWithTotalizersResults;
    	}
    	connectionOracle.disconnect();
        	
    	return loadedQueryData;
    }
    
    LoadedQueryData findDataByQueryFromMySQLDataBase(String finalQuery, String totalizersQuery) throws SQLException {
    	connectionMySQL = new ConnectionMySQL();
    	connectionMySQL.connect();
    	LoadedQueryData loadedQueryData = findDataByQuery(connectionMySQL.getIdConnection(), finalQuery);
    	
    	if (!totalizersQuery.isBlank()) {
    		ArrayList<String> totalizersResults = getTotalizersResults(connectionMySQL.getIdConnection(), totalizersQuery);
        	LoadedQueryData loadedQueryDataWithTotalizersResults = new LoadedQueryData(loadedQueryData.columnsNameAndNickName(), loadedQueryData.foundObjects(), totalizersResults);
        	connectionMySQL.disconnect();
        	        	
        	return loadedQueryDataWithTotalizersResults;
    	}
    	connectionMySQL.disconnect();
        	
    	return loadedQueryData;
    }
    
    int getActualTimeFromQueriesAnalysisFromOracleDataBase(String[] finalQueryAnalysis, String[] totalizersQueryAnalysis) throws SQLException {
		connectionOracle = new ConnectionOracle();
		connectionOracle.connect();
		int actualTimeFromFinalQuery = getActualTimeFromQueryAnalysisFromOracleDataBase(connectionOracle.getIdConnection(), finalQueryAnalysis);
		
		if (totalizersQueryAnalysis != null) {
			int actualTimeFromTotalizersQuery = getActualTimeFromQueryAnalysisFromOracleDataBase(connectionOracle.getIdConnection(), totalizersQueryAnalysis);
			connectionOracle.disconnect();
			
			return actualTimeFromFinalQuery + actualTimeFromTotalizersQuery;
		}
		connectionOracle.disconnect();
		
		return actualTimeFromFinalQuery;
	}
    
    int getActualTimeFromQueriesAnalysisFromMySQLDataBase(String finalQueryAnalysis, String totalizersQueryAnalysis) throws SQLException {
		connectionMySQL = new ConnectionMySQL();
		connectionMySQL.connect();
		int actualTimeFromFinalQuery = getActualTimeFromQueryAnalysisFromMySQLDataBase(connectionMySQL.getIdConnection(), finalQueryAnalysis);
    	
    	if (totalizersQueryAnalysis != null) {
    		int actualTimeFromTotalizersQuery = getActualTimeFromQueryAnalysisFromMySQLDataBase(connectionMySQL.getIdConnection(), totalizersQueryAnalysis);
        	connectionMySQL.disconnect();
    		
        	return actualTimeFromFinalQuery + actualTimeFromTotalizersQuery;
    	}
    	connectionMySQL.disconnect();
    	
    	return actualTimeFromFinalQuery;
	}


	Map<String, Map<String, String>> getTablesAndColumnsFromOracleDataBase() throws SQLException {
        connectionOracle = new ConnectionOracle();
        connectionOracle.connect();
		Map<String, Map<String, String>> tablesAndColumns = getTablesAndColumnsFromDataBase(connectionOracle.getIdConnection(), "ACADEMY", "%");
        connectionOracle.disconnect();
        
        return tablesAndColumns;
    }
    
    Map<String, Map<String, String>> getTablesAndColumnsFromMySQLDatabase() throws SQLException {
    	connectionMySQL = new ConnectionMySQL();
    	connectionMySQL.connect();
    	Map<String, Map<String, String>> tablesAndColumns = getTablesAndColumnsFromDataBase(connectionMySQL.getIdConnection(), "db_gerador_relatorio", "%");
    	connectionMySQL.disconnect();
    	
    	return tablesAndColumns;
    }

    ArrayList<RelationshipData> getRelationshipsFromOracleDataBase() throws SQLException {
        connectionOracle = new ConnectionOracle();
        connectionOracle.connect();
        String sql = "SELECT " +
                "  uc.TABLE_NAME, " +
                "  uc.COLUMN_NAME, " +
                "  uc.CONSTRAINT_NAME, " +
                "  rc.TABLE_NAME AS REFERENCED_TABLE_NAME, " +
                "  rc.COLUMN_NAME AS REFERENCED_COLUMN_NAME " +
                "FROM " +
                "  USER_CONS_COLUMNS uc " +
                "JOIN " +
                "  USER_CONSTRAINTS c ON uc.CONSTRAINT_NAME = c.CONSTRAINT_NAME " +
                "JOIN " +
                "  USER_CONS_COLUMNS rc ON c.R_CONSTRAINT_NAME = rc.CONSTRAINT_NAME " +
                "WHERE " +
                "  c.CONSTRAINT_TYPE = 'R' " +
                "ORDER BY " +
                "  uc.TABLE_NAME, uc.COLUMN_NAME";
        ArrayList<RelationshipData> listRelationshipData = getRelationshipsFromDataBase(connectionOracle.getIdConnection(), sql);
        connectionOracle.disconnect();
        
        return listRelationshipData;
    }
    
    ArrayList<RelationshipData> getRelationshipsFromMySQLDataBase() throws SQLException {
        connectionMySQL = new ConnectionMySQL();
        connectionMySQL.connect();
        String sql = "SELECT TABLE_NAME, COLUMN_NAME, CONSTRAINT_NAME, REFERENCED_TABLE_NAME, REFERENCED_COLUMN_NAME " +
                "FROM INFORMATION_SCHEMA.KEY_COLUMN_USAGE " +
                "WHERE REFERENCED_TABLE_NAME IS NOT NULL";
        ArrayList<RelationshipData> listRelationshipData = getRelationshipsFromDataBase(connectionMySQL.getIdConnection(), sql);
        connectionMySQL.disconnect();
        
        return listRelationshipData;
    }

    private LoadedQueryData findDataByQuery(Connection idConnection, String sql) throws SQLException {
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
    
    private ArrayList<String> getTotalizersResults(Connection idConnection, String totalizersQuery) throws SQLException {
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
    
    private int getActualTimeFromQueryAnalysisFromOracleDataBase(Connection idConnection, String[] query) throws SQLException {
    	command = idConnection.prepareStatement(query[0]);
    	command.execute();
    	command = idConnection.prepareStatement(query[1]);
    	data = command.executeQuery();
    	ArrayList<String> planDataLines = new ArrayList<>();
    	
    	while (data.next()) {
        	planDataLines.add(data.getString(1));
    	}
    	String planDataTimeLine = planDataLines.get(5);
    	Pattern pattern = Pattern.compile("\\|\\s(\\d{2}:\\d{2}:\\d{2})");
        Matcher matcher = pattern.matcher(planDataTimeLine);
        int seconds = 0;

        if (matcher.find()) {
        	seconds = TimeConverter.convertHHmmssToSeconds(matcher.group(1));
        } else {
        	throw new ActualTimeNotFoundException("Tempo da consulta não foi encontrado. Provavelmente o banco de dados Oracle está desatualizado");
        }
    	    	
    	return seconds;
    }
    
    private int getActualTimeFromQueryAnalysisFromMySQLDataBase(Connection idConnection, String query) throws SQLException {
    	command = idConnection.prepareStatement(query);
    	data = command.executeQuery();
    	data.next();
    	String allData = data.getString(1);
    	String firstLineData = allData.split("\n", 0)[1];
    	Pattern pattern = Pattern.compile("actual time=(\\d+\\.\\d+)\\.\\.(\\d+\\.\\d+)");
    	Matcher matcher = pattern.matcher(firstLineData);
    	int startTime = 0;
    	int endTime = 0;
    	
    	if (matcher.find()) {
    		startTime = Integer.parseInt(matcher.group(1));
        	endTime = Integer.parseInt(matcher.group(2));
    	}
    	
    	return (startTime + endTime) / 2;
    }
    
    private Map<String, Map<String, String>> getTablesAndColumnsFromDataBase(Connection idConnection, String catalog, String tableNamePattern) throws SQLException {
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
    
    private ArrayList<RelationshipData> getRelationshipsFromDataBase(Connection idConnection, String sql) throws SQLException {
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