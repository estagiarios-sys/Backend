package com.systextil.relatorio.domain.data_base_data;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.stereotype.Repository;

import com.systextil.relatorio.infra.data_base_connection.ConnectionMySQL;

@Repository
public class MySqlDataBaseDataRepository extends DataBaseDataRepository {
	
    private ConnectionMySQL connectionMySQL;
    
    LoadedQueryData findDataByQuery(String finalQuery, String totalizersQuery) throws SQLException {
    	connectionMySQL = new ConnectionMySQL();
    	connectionMySQL.connect();
    	LoadedQueryData loadedQueryData = super.findDataByQuery(connectionMySQL.getIdConnection(), finalQuery, totalizersQuery);
    	connectionMySQL.disconnect();
        	
    	return loadedQueryData;
    }
    
    int getActualTimeFromQueriesAnalysisFromDataBase(String finalQueryAnalysis, String totalizersQueryAnalysis) throws SQLException {
		connectionMySQL = new ConnectionMySQL();
		connectionMySQL.connect();
		int actualTimeFromFinalQuery = getActualTimeFromQueryAnalysisFromDataBase(connectionMySQL.getIdConnection(), finalQueryAnalysis);
    	
    	if (totalizersQueryAnalysis != null) {
    		int actualTimeFromTotalizersQuery = getActualTimeFromQueryAnalysisFromDataBase(connectionMySQL.getIdConnection(), totalizersQueryAnalysis);
        	connectionMySQL.disconnect();
    		
        	return actualTimeFromFinalQuery + actualTimeFromTotalizersQuery;
    	}
    	connectionMySQL.disconnect();
    	
    	return actualTimeFromFinalQuery;
	}
    
    Map<String, Map<String, String>> getTablesAndColumnsFromDataBase() throws SQLException {
    	connectionMySQL = new ConnectionMySQL();
    	connectionMySQL.connect();
    	Map<String, Map<String, String>> tablesAndColumns = super.getTablesAndColumnsFromDataBase(connectionMySQL.getIdConnection(), "db_gerador_relatorio", "%");
    	connectionMySQL.disconnect();
    	
    	return tablesAndColumns;
    }
    
    ArrayList<RelationshipData> getRelationshipsFromDataBase() throws SQLException {
        connectionMySQL = new ConnectionMySQL();
        connectionMySQL.connect();
        String sql = "SELECT TABLE_NAME, COLUMN_NAME, CONSTRAINT_NAME, REFERENCED_TABLE_NAME, REFERENCED_COLUMN_NAME " +
                "FROM INFORMATION_SCHEMA.KEY_COLUMN_USAGE " +
                "WHERE REFERENCED_TABLE_NAME IS NOT NULL";
        ArrayList<RelationshipData> listRelationshipData = super.getRelationshipsFromDataBase(connectionMySQL.getIdConnection(), sql);
        connectionMySQL.disconnect();
        
        return listRelationshipData;
    }
    
    private int getActualTimeFromQueryAnalysisFromDataBase(Connection idConnection, String query) throws SQLException {
    	PreparedStatement command = null;
    	
    	try {
    		command = idConnection.prepareStatement(query);
        	ResultSet data = command.executeQuery();
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
    	} catch (SQLException exception) {
    		throw new SQLException();
    	} finally {
    		if (command != null) {
    			command.close();
    		}
		}
    }
}