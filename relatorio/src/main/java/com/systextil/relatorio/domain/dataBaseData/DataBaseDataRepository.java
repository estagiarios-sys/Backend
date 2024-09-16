package com.systextil.relatorio.domain.dataBaseData;

import com.systextil.relatorio.infra.dataBaseConnection.ConnectionMySQL;
import com.systextil.relatorio.infra.dataBaseConnection.ConnectionOracle;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

class DataBaseDataRepository {

    private ConnectionMySQL connectionMySQL;
    private ConnectionOracle connectionOracle;

    LoadedQueryData findDataByQueryFromOracleDatabase(String finalQuery, QueryWithTotalizers queryWithTotalizers) throws ClassNotFoundException, SQLException {
    	connectionOracle = new ConnectionOracle();
    	connectionOracle.connect();
    	LoadedQueryData loadedQueryData = findDataByQuery(connectionOracle.getIdConnection(), finalQuery);
    	
    	try {
    		queryWithTotalizers.query();
    		ArrayList<String> totalizersResults = getTotalizersResults(connectionOracle.getIdConnection(), queryWithTotalizers);
        	LoadedQueryData loadedQueryDataWithTotalizersResults = new LoadedQueryData(loadedQueryData.columnsNameAndNickName(), loadedQueryData.foundObjects(), totalizersResults);
        	connectionOracle.disconnect();
        	
        	return loadedQueryDataWithTotalizersResults;
    	} catch (NullPointerException e) {
    		connectionOracle.disconnect();
        	
        	return loadedQueryData;
    	}
    }
    
    LoadedQueryData findDataByQueryFromMySQLDatabase(String finalQuery, QueryWithTotalizers queryWithTotalizers) throws ClassNotFoundException, SQLException {
    	connectionMySQL = new ConnectionMySQL();
    	connectionMySQL.connect();
    	LoadedQueryData loadedQueryData = findDataByQuery(connectionMySQL.getIdConnection(), finalQuery);
    	
    	try {
    		queryWithTotalizers.query();
    		ArrayList<String> totalizersResults = getTotalizersResults(connectionMySQL.getIdConnection(), queryWithTotalizers);
        	LoadedQueryData loadedQueryDataWithTotalizersResults = new LoadedQueryData(loadedQueryData.columnsNameAndNickName(), loadedQueryData.foundObjects(), totalizersResults);
        	connectionMySQL.disconnect();
        	
        	return loadedQueryDataWithTotalizersResults;
    	} catch (NullPointerException e) {
    		connectionMySQL.disconnect();
        	
        	return loadedQueryData;
    	}
    }

    Map<String, String[]> getTablesAndColumnsFromOracleDatabase() throws ClassNotFoundException, SQLException {
        connectionOracle = new ConnectionOracle();
        connectionOracle.connect();
        Map<String, String[]> tablesAndColumns = getTablesAndColumnsFromDatabase(connectionOracle.getIdConnection(), "deVS", "BASI%");
        connectionOracle.disconnect();
        
        return tablesAndColumns;
    }
    
    Map<String, String[]> getTablesAndColumnsFromMySQLDatabase() throws ClassNotFoundException, SQLException {
    	connectionMySQL = new ConnectionMySQL();
    	connectionMySQL.connect();
    	Map<String, String[]> tablesAndColumns = getTablesAndColumnsFromDatabase(connectionMySQL.getIdConnection(), "db_gerador_relatorio", "%");
    	connectionMySQL.disconnect();
    	
    	return tablesAndColumns;
    }

    ArrayList<RelationshipData> getRelationshipsFromOracleDatabase() throws SQLException, ClassNotFoundException {
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
                "AND uc.TABLE_NAME LIKE 'BASI%' " +
                "AND rc.TABLE_NAME LIKE 'BASI%' " +
                "ORDER BY " +
                "  uc.TABLE_NAME, uc.COLUMN_NAME";
        ArrayList<RelationshipData> listRelationshipData = getRelationshipsFromDatabase(connectionOracle.getIdConnection(), sql);
        connectionOracle.disconnect();
        
        return listRelationshipData;
    }
    
    ArrayList<RelationshipData> getRelationshipsFromMySQLDatabase() throws SQLException, ClassNotFoundException {
        connectionMySQL = new ConnectionMySQL();
        connectionMySQL.connect();
        String sql = "SELECT TABLE_NAME, COLUMN_NAME, CONSTRAINT_NAME, REFERENCED_TABLE_NAME, REFERENCED_COLUMN_NAME " +
                "FROM INFORMATION_SCHEMA.KEY_COLUMN_USAGE " +
                "WHERE REFERENCED_TABLE_NAME IS NOT NULL";
        ArrayList<RelationshipData> listRelationshipData = getRelationshipsFromDatabase(connectionMySQL.getIdConnection(), sql);
        connectionMySQL.disconnect();
        
        return listRelationshipData;
    }
    
    private LoadedQueryData findDataByQuery(Connection idConnection, String sql) throws SQLException, ClassNotFoundException {
        ArrayList<Object[]> listObjects = new ArrayList<>();
        Map<String, String> columnsNameAndNickName = new HashMap<>();
        PreparedStatement command = idConnection.prepareStatement(sql);
        ResultSet data = command.executeQuery();
        ResultSetMetaData metaData = data.getMetaData();
        int columnsNumber = metaData.getColumnCount();
        
        for (int i = 1; i <= columnsNumber; i++) {
        	String columnNickName = metaData.getColumnLabel(i);
        	String columnTableName = metaData.getTableName(i);
        	String columnName = metaData.getColumnName(i);
        	        	
        	if (columnNickName.equalsIgnoreCase(columnName)) {
        		columnsNameAndNickName.put(columnTableName + "." + columnName, null);
        	} else {
        		columnsNameAndNickName.put(columnTableName + "." + columnName, columnNickName);
        	}
        }

        while (data.next()) {
            Object[] object = new Object[columnsNumber];
            
            for (int i = 1; i <= columnsNumber; i++) {
                object[i - 1] = data.getString(i);
            }
            listObjects.add(object);
        }
        LoadedQueryData loadedQueryData = new LoadedQueryData(columnsNameAndNickName, listObjects, null);
                
        return loadedQueryData;
    }
    
    private ArrayList<String> getTotalizersResults(Connection idConnection, QueryWithTotalizers queryWithTotalizers) throws SQLException {
    	ArrayList<String> totalizersResults = new ArrayList<>();
    	PreparedStatement command = idConnection.prepareStatement(queryWithTotalizers.query());
    	ResultSet data = command.executeQuery();
    	ResultSetMetaData metaData = data.getMetaData();
    	int columnsNumber = metaData.getColumnCount();
    	Totalizer totalizer;
    	data.next();
    	
    	for (int i = 1; i <= columnsNumber; i++) {
    		totalizer = queryWithTotalizers.totalizers().get(i-1);
    		totalizersResults.add(totalizer.toPortuguese() + ": " + data.getInt(i));
    	}
    	    	
    	return totalizersResults;
    }
    
    private Map<String, String[]> getTablesAndColumnsFromDatabase(Connection idConnection, String catalog, String tableNamePattern) throws SQLException {
        Map<String, String[]> tablesAndColumns = new HashMap<>();
        DatabaseMetaData metaData = idConnection.getMetaData();
        ResultSet tables = metaData.getTables(catalog, null, tableNamePattern, new String[]{"TABLE"});

        while (tables.next()) {
            String tableName = tables.getString("TABLE_NAME");

            ResultSet columns = metaData.getColumns(null, null, tableName, "%");
            ArrayList<String> columnNames = new ArrayList<>();
            while (columns.next()) {
                String columnName = columns.getString("COLUMN_NAME");
                if (columnName != null) {
                    columnNames.add(columnName);
                }
            }
            tablesAndColumns.put(tableName, columnNames.toArray(new String[0]));
        }

        return tablesAndColumns;
    }
    
    private ArrayList<RelationshipData> getRelationshipsFromDatabase(Connection idConnection, String sql) throws SQLException {
    	PreparedStatement comando = idConnection.prepareStatement(sql);
        ResultSet dados = comando.executeQuery();
        ArrayList<RelationshipData> listRelationshipData = new ArrayList<>();

        while (dados.next()) {
            String tableName = dados.getString("TABLE_NAME");
            String columnName = dados.getString("COLUMN_NAME");
            String referencedTableName = dados.getString("REFERENCED_TABLE_NAME");
            String referencedColumnName = dados.getString("REFERENCED_COLUMN_NAME");
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