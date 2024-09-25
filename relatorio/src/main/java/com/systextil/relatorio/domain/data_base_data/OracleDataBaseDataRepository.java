package com.systextil.relatorio.domain.data_base_data;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.systextil.relatorio.infra.data_base_connection.ConnectionOracle;

@Repository
public class OracleDataBaseDataRepository extends DataBaseDataRepository {

    private ConnectionOracle connectionOracle;
	
	LoadedQueryData findDataByQuery(String finalQuery, String totalizersQuery) throws SQLException {
    	connectionOracle = new ConnectionOracle();
    	connectionOracle.connect();
    	LoadedQueryData loadedQueryData = super.findDataByQuery(connectionOracle.getIdConnection(), finalQuery, totalizersQuery);
    	connectionOracle.disconnect();
        	
    	return loadedQueryData;
    }
	
	int getActualTimeFromQueriesAnalysisFromDataBase(String[] finalQueryAnalysis, String[] totalizersQueryAnalysis) throws SQLException {
		connectionOracle = new ConnectionOracle();
		connectionOracle.connect();
		int actualTimeFromFinalQuery = getActualTimeFromQueryAnalysisFromDataBase(connectionOracle.getIdConnection(), finalQueryAnalysis);
		
		if (totalizersQueryAnalysis != null) {
			int actualTimeFromTotalizersQuery = getActualTimeFromQueryAnalysisFromDataBase(connectionOracle.getIdConnection(), totalizersQueryAnalysis);
			connectionOracle.disconnect();
			
			return actualTimeFromFinalQuery + actualTimeFromTotalizersQuery;
		}
		connectionOracle.disconnect();
		
		return actualTimeFromFinalQuery;
	}
	
	Map<String, Map<String, String>> getTablesAndColumnsFromDataBase() throws SQLException {
        connectionOracle = new ConnectionOracle();
        connectionOracle.connect();
		Map<String, Map<String, String>> tablesAndColumns = super.getTablesAndColumnsFromDataBase(connectionOracle.getIdConnection(), "ACADEMY", "%");
        connectionOracle.disconnect();
        
        return tablesAndColumns;
    }
	
	ArrayList<RelationshipData> getRelationshipsFromDataBase() throws SQLException {
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
        ArrayList<RelationshipData> listRelationshipData = super.getRelationshipsFromDataBase(connectionOracle.getIdConnection(), sql);
        connectionOracle.disconnect();
        
        return listRelationshipData;
    }
	
	private int getActualTimeFromQueryAnalysisFromDataBase(Connection idConnection, String[] query) throws SQLException {
		PreparedStatement explainPlanForCommand = null;
		PreparedStatement planDisplayCommand = null;
		
		try {
			explainPlanForCommand = idConnection.prepareStatement(query[0]);
	    	explainPlanForCommand.execute();
	    	planDisplayCommand = idConnection.prepareStatement(query[1]);
	    	ResultSet data = planDisplayCommand.executeQuery();
	    	data.next();
	    	    	
	    	return data.getInt(1);
		} catch (SQLException exception) {
			throw new SQLException(exception.getLocalizedMessage());
		} finally {
			if (explainPlanForCommand != null) {
				explainPlanForCommand.close();
			}
			if (planDisplayCommand != null) {
				planDisplayCommand.close();
			}
		}
    }
}